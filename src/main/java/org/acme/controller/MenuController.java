package org.acme.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.dto.CreateMenuRequestDto;
import org.acme.service.MenuService;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;

@Path("/api/v1/menu")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MenuController {

    @Inject
    MenuService menuService;

    @POST
    // @SecurityRequirement(name = "bearerAuth")
    public Response createMenu(CreateMenuRequestDto request) {
        return menuService.createMenu(request);
    }
}
