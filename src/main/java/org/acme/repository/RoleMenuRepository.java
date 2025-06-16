package org.acme.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.entity.RoleMenu;

@ApplicationScoped
public class RoleMenuRepository implements PanacheRepository<RoleMenu> {

}
