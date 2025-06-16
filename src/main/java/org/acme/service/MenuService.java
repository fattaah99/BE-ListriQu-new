package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import org.acme.dto.CreateMenuRequestDto;
import org.acme.entity.MasterMenu;
import org.acme.repository.MasterMenuRepository;
import org.acme.util.ErrorResponse;
import org.acme.util.SuccessResponse;

import java.time.LocalDateTime;

@ApplicationScoped
public class MenuService {

    @Inject
    MasterMenuRepository menuRepository;

    @Transactional
    public Response createMenu(CreateMenuRequestDto request) {
        try {
            MasterMenu menu = new MasterMenu();
            menu.menu_code = request.menu_code;
            menu.menu_name = request.menu_name;
            menu.menu_icon = request.menu_icon;
            menu.menu_url = request.menu_url;
            menu.menu_order = request.menu_order;
            menu.is_active = MasterMenu.Status.Active;
            menu.created_at = LocalDateTime.now();
            menu.updated_at = LocalDateTime.now();
            menu.created_by = request.created_by;
            menu.updated_by = request.updated_by;

            if (request.parent_id != null) {
                MasterMenu parent = menuRepository.findById(request.parent_id);
                if (parent == null) {
                    return ErrorResponse.badRequest("Invalid parent_id");
                }
                menu.parent = parent;
            }

            menuRepository.persist(menu);
            return SuccessResponse.ok("Menu created successfully");
        } catch (Exception e) {
            return ErrorResponse.serverError("Failed to create menu", e);
        }
    }
}
