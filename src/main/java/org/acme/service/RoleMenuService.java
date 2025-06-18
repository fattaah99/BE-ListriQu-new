package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import org.acme.repository.MasterMenuRepository;
import org.acme.repository.MasterRoleRepository;
import org.acme.repository.RoleMenuRepository;
import org.acme.dto.AssignRoleMenuRequestDto;
import org.acme.entity.MasterMenu;
import org.acme.entity.MasterRole;
import org.acme.entity.RoleMenu;
import org.acme.response.GetRoleMenuResponse;
import org.acme.util.ErrorResponse;
import org.acme.util.SuccessResponse;
import org.acme.repository.MasterRoleRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class RoleMenuService {

    @Inject
    RoleMenuRepository roleMenuRepository;

    @Inject
    MasterMenuRepository masterMenuRepository;

    @Inject
    MasterRoleRepository masterRoleRepository;

    @Transactional
    public Response getMenusByRole(Long roleId) {
       try {
        List<GetRoleMenuResponse.MenuDto> menus = roleMenuRepository.findByRoleIdAndActive(roleId).stream()
            .map(rm -> {
                MasterMenu menu = rm.menu;
                GetRoleMenuResponse.MenuDto dto = new GetRoleMenuResponse.MenuDto();
                dto.menu_id = menu.menu_id != null ? menu.menu_id.longValue() : null;
                dto.menu_name = menu.menu_name;
                dto.menu_url = menu.menu_url;
                dto.menu_icon = menu.menu_icon;
                dto.menu_order = menu.menu_order;
                dto.parent_id = (menu.parent != null && menu.parent.menu_id != null) ? menu.parent.menu_id.longValue() : null;
                return dto;
            })
            .sorted((m1, m2) -> Integer.compare(m1.menu_order, m2.menu_order))
            .collect(Collectors.toList());

        GetRoleMenuResponse response = new GetRoleMenuResponse();
        response.status = "success";
        // response.message = "Menu fetched successfully";
        response.data = menus;

        return Response.ok(response).build();

    } catch (Exception e) {
        return org.acme.util.ErrorResponse.serverError("Failed to fetch role menu", e);
    }
}

@Transactional
    public Response assignMenusToRole(AssignRoleMenuRequestDto request) {
        try {
            MasterRole role = masterRoleRepository.findById(request.role_id);
            if (role == null) {
                return ErrorResponse.badRequest("Invalid role_id");
            }

            // Hapus semua role-menu lama
            roleMenuRepository.delete("role.role_id = ?1", request.role_id);

            // Tambahkan role-menu baru
            for (Long menuId : request.menu_ids) {
                MasterMenu menu = masterMenuRepository.findById(menuId);
                if (menu != null) {
                    RoleMenu rm = new RoleMenu();
                    rm.role = role;
                    rm.menu = menu;
                    rm.is_active = RoleMenu.Status.Active;
                    rm.created_at = LocalDateTime.now();
                    rm.updated_at = LocalDateTime.now();
                    roleMenuRepository.persist(rm);
                }
            }

            return SuccessResponse.ok("Role menu assigned successfully");
        } catch (Exception e) {
            return ErrorResponse.serverError("Failed to assign role menu", e);
        }
    }
}
