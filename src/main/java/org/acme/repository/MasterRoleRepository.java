package org.acme.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.entity.MasterRole;

@ApplicationScoped
public class MasterRoleRepository implements PanacheRepository<MasterRole> {
}
