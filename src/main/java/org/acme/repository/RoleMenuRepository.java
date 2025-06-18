package org.acme.repository;

import jakarta.enterprise.context.ApplicationScoped;
import org.acme.entity.RoleMenu;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import java.util.List;

@ApplicationScoped
public class RoleMenuRepository implements PanacheRepository<RoleMenu> {

    public List<RoleMenu> findByRoleIdAndActive(Long roleId) {
        return find("role.role_id = ?1 AND is_active = 'Active'", roleId).list();
    }

}
