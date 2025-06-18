package org.acme.controller;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.dto.LoginRequestDto;
import org.acme.dto.RegisterRequestDto;
import org.acme.service.AuthService;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;


@SecurityScheme(
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT"
)

@Path("/api/v1/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthController {

    @Inject
    AuthService authService;

    @POST
    
    @Path("/login")
    public Response login(LoginRequestDto request) {
        return authService.login(request);
    }

    @POST
    @RolesAllowed("SUPERADMIN")
    @Path("/logout")
    public Response logout(@HeaderParam("Authorization") String authHeader) {
        return authService.logout(authHeader);
    }

    @POST
@Path("/register")
public Response register(RegisterRequestDto request) {
    return authService.register(request);
}


@GET
@Path("/admin-data")
@RolesAllowed("SUPERADMIN")
public Response getSuperAdminData() {
    return Response.ok("Data rahasia untuk SUPERADMIN").build();
}


}
