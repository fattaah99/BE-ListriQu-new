package org.acme.service;

import io.smallrye.jwt.build.Jwt;
import io.smallrye.jwt.auth.principal.JWTParser;
import io.smallrye.jwt.auth.principal.ParseException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import org.acme.dto.LoginRequestDto;
import org.acme.dto.LoginResponseDto;
import org.acme.dto.LoginResponseDto.Data;
import org.acme.entity.MasterMenu;
import org.acme.entity.MasterUser;
import org.acme.entity.UserSession;
import org.acme.repository.MasterMenuRepository;
import org.acme.repository.MasterUserRepository;
import org.acme.repository.UserSessionRepository;

import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class AuthService {

    @Inject
    MasterUserRepository userRepository;

    @Inject
    UserSessionRepository sessionRepository;

    @Inject
    MasterMenuRepository menuRepository;

    @Inject
    JWTParser jwtParser;

    @Transactional
    public Response login(LoginRequestDto request) {
        MasterUser user = userRepository.findByUsernameOrEmail(request.username);
        if (user == null || !user.status.equals(MasterUser.Status.Active)) {
            return unauthorized();
        }

        if (!request.password.equals(user.password)) {
            return unauthorized();
        }

        String token = Jwt.issuer("your-app")
                .upn(user.username)
                .claim("user_id", user.user_id)
                .claim("role_id", user.role.role_id)
                .claim("groups", List.of(user.role.role_code))  // <= penting untuk @RolesAllowed
                .expiresIn(24 * 60 * 60)
                .sign();

        UserSession session = new UserSession();
        session.user = user;
        session.session_token = token;
        session.login_at = LocalDateTime.now();
        session.status = UserSession.Status.Active;
        sessionRepository.persist(session);

        List<MasterMenu> menus = menuRepository.findByRoleId(user.role.role_id);
        List<java.util.Map<String, Object>> menuTree = menuRepository.buildMenuTree(menus);

        Data data = new Data();
        data.user_id = user.user_id;
        data.username = user.username;
        data.full_name = user.full_name;
        data.role_id = user.role.role_id;
        data.token = token;
        data.menu = menuTree;

        LoginResponseDto response = new LoginResponseDto();
        response.status = "success";
        response.message = "Login successful";
        response.data = data;

        return Response.ok(response).build();
    }

    @Transactional
    public Response logout(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(java.util.Map.of("status", "error", "message", "Missing token"))
                    .build();
        }

        String token = authHeader.substring("Bearer ".length());

        try {
            var jwt = jwtParser.parse(token);
            Integer userId = jwt.getClaim("user_id");

            UserSession session = sessionRepository.find("user.user_id = ?1 AND session_token = ?2 AND status = 'Active'",
                    userId, token).firstResult();

            if (session != null) {
                session.status = UserSession.Status.Logout;
                session.logout_at = LocalDateTime.now();
            }

            return Response.ok(java.util.Map.of("status", "success", "message", "Logout successful")).build();

        } catch (ParseException e) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(java.util.Map.of("status", "error", "message", "Invalid token"))
                    .build();
        }
    }

    private Response unauthorized() {
        return Response.status(Response.Status.UNAUTHORIZED)
                .entity(java.util.Map.of("status", "error", "message", "Invalid credentials"))
                .build();
    }
} 
