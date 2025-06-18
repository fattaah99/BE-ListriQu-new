package org.acme.controller;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.acme.dto.AssignRoleMenuRequestDto;
import org.acme.service.RoleMenuService;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;

@Path("/api/v1/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RoleMenuController {

    @Inject
    RoleMenuService roleMenuService;

    @GET
    @RolesAllowed("SUPERADMIN")
    @Path("/role/{role_id}/menu")
    public Response getMenusByRole(@PathParam("role_id") Long roleId) {
        return roleMenuService.getMenusByRole(roleId);
    }

    @POST
    @RolesAllowed("SUPERADMIN")
    @Path("/role-menu")
   
    public Response assignMenusToRole(AssignRoleMenuRequestDto request) {
        return roleMenuService.assignMenusToRole(request);
    }
}
