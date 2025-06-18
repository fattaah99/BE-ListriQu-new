package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import org.acme.dto.CreateUnitRequestDto;
import org.acme.dto.UpdateUnitRequestDto;
import org.acme.entity.MasterUnit;
import org.acme.entity.MasterUser;
import org.acme.repository.MasterUnitRepository;
import org.acme.repository.MasterUserRepository;
import org.acme.util.ErrorResponse;
import org.acme.util.SuccessResponse;

import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class MasterUnitService {

    @Inject
    MasterUnitRepository unitRepository;

    @Inject
    MasterUserRepository userRepository;

    public Response getAll() {
        List<MasterUnit> units = unitRepository.listAll();
        return SuccessResponse.ok("Success", units);
    }

    public Response getById(Integer id) {
        MasterUnit unit = unitRepository.findById(id);
        if (unit == null) return ErrorResponse.notFound("Unit not found");
        return SuccessResponse.ok("Success", unit);
    }

    @Transactional
    public Response create(CreateUnitRequestDto request) {
        try {
            MasterUnit unit = new MasterUnit();
            unit.unit_code = request.unit_code;
            unit.unit_name = request.unit_name;
            unit.description = request.description;
            unit.address = request.address;
            unit.phone = request.phone;
            unit.email = request.email;
            unit.created_by = request.created_by;
            unit.updated_by = request.updated_by;
            unit.created_at = LocalDateTime.now();
            unit.updated_at = LocalDateTime.now();

            if (request.status != null) {
                unit.status = MasterUnit.Status.valueOf(request.status);
            }

            if (request.parent_unit_id != null) {
                unit.parent_unit = unitRepository.findById(request.parent_unit_id);
            }

            if (request.manager_id != null) {
                MasterUser manager = userRepository.findById(request.manager_id);
                unit.manager = manager;
            }

            unitRepository.persist(unit);
            return SuccessResponse.ok("Unit created successfully");

        } catch (Exception e) {
            return ErrorResponse.serverError("Failed to create unit", e);
        }
    }

    @Transactional
    public Response update(Integer id, UpdateUnitRequestDto request) {
        try {
            MasterUnit unit = unitRepository.findById(id);
            if (unit == null) return ErrorResponse.notFound("Unit not found");

            unit.unit_code = request.unit_code;
            unit.unit_name = request.unit_name;
            unit.description = request.description;
            unit.address = request.address;
            unit.phone = request.phone;
            unit.email = request.email;

            if (request.status != null) {
                unit.status = MasterUnit.Status.valueOf(request.status);
            }

            if (request.parent_unit_id != null) {
                unit.parent_unit = unitRepository.findById(request.parent_unit_id);
            }

            if (request.manager_id != null) {
                unit.manager = userRepository.findById(request.manager_id);
            }

            unit.updated_by = request.updated_by;
            unit.updated_at = LocalDateTime.now();

            return SuccessResponse.ok("Unit updated successfully");

        } catch (Exception e) {
            e.printStackTrace();
            return ErrorResponse.serverError("Failed to update unit", e);
        }
    }

    @Transactional
    public Response delete(Integer id) {
        boolean deleted = unitRepository.deleteById(id);
        if (!deleted) return ErrorResponse.notFound("Unit not found");
        return SuccessResponse.ok("Unit deleted successfully");
    }
}
