package org.acme.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.acme.entity.MasterMenu;

import java.util.*;
import java.util.stream.Collectors;

@ApplicationScoped
public class MasterMenuRepository {

    @Inject
    EntityManager em;

    public List<MasterMenu> findByRoleId(int roleId) {
        return em.createQuery("""
            SELECT m FROM RoleMenu rm
            JOIN rm.menu m
            WHERE rm.role.role_id = :roleId AND rm.is_active = 'Active' AND m.is_active = 'Active'
            ORDER BY m.parent.menu_id NULLS FIRST, m.menu_order
        """, MasterMenu.class)
        .setParameter("roleId", roleId)
        .getResultList();
    }

    public List<Map<String, Object>> buildMenuTree(List<MasterMenu> flatMenus) {
        Map<Integer, Map<String, Object>> menuMap = new HashMap<>();
        List<Map<String, Object>> rootMenus = new ArrayList<>();

        for (MasterMenu menu : flatMenus) {
            Map<String, Object> node = new HashMap<>();
            node.put("menu_id", menu.menu_id);
            node.put("menu_name", menu.menu_name);
            node.put("menu_url", menu.menu_url);
            node.put("children", new ArrayList<>());
            menuMap.put(menu.menu_id, node);
        }

        for (MasterMenu menu : flatMenus) {
            Map<String, Object> node = menuMap.get(menu.menu_id);
            if (menu.parent != null && menuMap.containsKey(menu.parent.menu_id)) {
                List<Map<String, Object>> children = (List<Map<String, Object>>) menuMap.get(menu.parent.menu_id).get("children");
                children.add(node);
            } else {
                rootMenus.add(node);
            }
        }

        return rootMenus;
    }
}