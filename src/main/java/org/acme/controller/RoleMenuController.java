package org.acme.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.service.RoleMenuService;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;

@Path("/api/v1/role")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RoleMenuController {

    @Inject
    RoleMenuService roleMenuService;

    @GET
    @Path("/{role_id}/menu")
    public Response getMenusByRole(@PathParam("role_id") Long roleId) {
        return roleMenuService.getMenusByRole(roleId);
    }
}
