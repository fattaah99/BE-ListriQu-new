package org.acme.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.dto.LoginRequestDto;
import org.acme.service.AuthService;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;


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
    @SecurityRequirement(name = "bearerAuth")  // Harus sama persis
@Path("/logout")
public Response logout(@HeaderParam("Authorization") String authHeader) {
    return authService.logout(authHeader);
}

}
