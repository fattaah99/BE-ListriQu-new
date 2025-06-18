package org.acme.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.entity.MasterUser;

@ApplicationScoped
public class MasterUserRepository implements PanacheRepositoryBase<MasterUser, Integer>
{
    public MasterUser findByUsernameOrEmail(String input) {
        return find("username = ?1 OR email = ?1", input).firstResult();
    }
}
