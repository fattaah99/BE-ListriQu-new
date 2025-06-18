package org.acme.controller;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.acme.dto.CreateUnitRequestDto;
import org.acme.dto.UpdateUnitRequestDto;
import org.acme.entity.MasterUnit;
import org.acme.service.MasterUnitService;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;

@Path("/api/v1/unit")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MasterUnitController {

    @Inject
    MasterUnitService unitService;

    @GET
    @RolesAllowed("SUPERADMIN")
    public Response getAll() {
        return unitService.getAll();
    }

    @GET
    @RolesAllowed("SUPERADMIN")
    @Path("/{id}")
    public Response getById(@PathParam("id") Integer id) {
        return unitService.getById(id);
    }

    @POST
    @RolesAllowed("SUPERADMIN")
    public Response create(CreateUnitRequestDto request) {
        return unitService.create(request);
    }

    @PUT
    @RolesAllowed("SUPERADMIN")
    @Path("/{id}")

    public Response update(@PathParam("id") Integer id, UpdateUnitRequestDto request) {
        return unitService.update(id, request);
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Integer id) {
        return unitService.delete(id);
    }
}
