package org.acme.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.entity.UserSession;

@ApplicationScoped
public class UserSessionRepository implements PanacheRepository<UserSession> {
}
