package org.acme.service;

import io.smallrye.jwt.build.Jwt;
import io.smallrye.jwt.auth.principal.JWTParser;
import io.smallrye.jwt.auth.principal.ParseException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.JsonNumber;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import org.acme.dto.LoginRequestDto;
import org.acme.dto.RegisterRequestDto;
import org.acme.entity.MasterMenu;
import org.acme.entity.MasterRole;
import org.acme.entity.MasterUser;
import org.acme.entity.UserSession;
import org.acme.repository.MasterMenuRepository;
import org.acme.repository.MasterRoleRepository;
import org.acme.repository.MasterUserRepository;
import org.acme.repository.UserSessionRepository;
import org.acme.response.LoginResponseDto;
import org.acme.response.LoginResponseDto.Data;
import org.acme.util.ErrorResponse;
import io.vertx.ext.web.RoutingContext;
import jakarta.ws.rs.core.Context;
import org.acme.util.SuccessResponse;
import org.mindrot.jbcrypt.BCrypt;
import org.acme.repository.RoleMenuRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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

    @Inject
    MasterRoleRepository roleRepository;

    // @Context
    // RoleMenuRepository roleRepository;

    @Context
    RoutingContext routingContext;

    @Transactional
    public Response register(RegisterRequestDto request) {
        if (userRepository.findByUsernameOrEmail(request.username) != null ||
                userRepository.find("email", request.email).firstResult() != null) {
            return ErrorResponse.badRequest("Username or email already exists");
        }

        MasterRole role = roleRepository.findById(request.role_id.longValue());
        if (role == null) {
            return ErrorResponse.badRequest("Invalid role_id");
        }

        MasterUser user = new MasterUser();
        user.username = request.username;
        user.email = request.email;
        user.password = BCrypt.hashpw(request.password, BCrypt.gensalt());
        user.full_name = request.full_name;
        user.phone = request.phone;
        user.unit_id = request.unit_id;
        user.role = role;
        user.status = MasterUser.Status.Active;
        user.created_at = LocalDateTime.now();
        user.updated_at = LocalDateTime.now();
        user.created_by = request.created_by;
        user.updated_by = request.created_by;

        userRepository.persist(user);

        return SuccessResponse.ok("Registration successful");
    }

    @Transactional
    public Response login(LoginRequestDto request) {
        MasterUser user = userRepository.findByUsernameOrEmail(request.username);
        if (user == null || !user.status.equals(MasterUser.Status.Active)) {
            return ErrorResponse.unauthorized("Invalid credentials");
        }

        if (!BCrypt.checkpw(request.password, user.password)) {
            return ErrorResponse.unauthorized("Invalid credentials");
        }

        String ipAddress = routingContext.request().remoteAddress().host();
        String userAgent = routingContext.request().getHeader("User-Agent");

        String token = Jwt.issuer("your-app")
                .upn(user.username)
                .claim("user_id", user.user_id)
                .claim("role_id", user.role.role_id)
                .claim("groups", List.of(user.role.role_code))
                .expiresIn(24 * 60 * 60)
                .sign();

        UserSession session = new UserSession();
        session.user = user;
        session.session_token = token;
        session.login_at = LocalDateTime.now();
        session.status = UserSession.Status.Active;
        session.ip_address = ipAddress;
        session.user_agent = userAgent;
        sessionRepository.persist(session);

        List<MasterMenu> menus = menuRepository.findByRoleId(user.role.role_id);
        List<Map<String, Object>> menuTree = menuRepository.buildMenuTree(menus);

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
            return ErrorResponse.unauthorized("Missing token");
        }

        String token = authHeader.substring("Bearer ".length());

        try {
            var jwt = jwtParser.parse(token);
            JsonNumber jsonNumber = jwt.getClaim("user_id");
            Integer userId = jsonNumber.intValue();

            UserSession session = sessionRepository.find(
                    "user.user_id = ?1 AND session_token = ?2 AND status = 'Active'",
                    userId, token).firstResult();

            if (session != null) {
                session.status = UserSession.Status.Logout;
                session.logout_at = LocalDateTime.now();
            }

            return Response.ok(Map.of("status", "success", "message", "Logout successful")).build();

        } catch (ParseException e) {
            return ErrorResponse.unauthorized("Invalid token");
        }
    }
}
