package com.bicart.controller;

import java.util.Map;
import java.util.Set;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bicart.dto.RoleDto;
import com.bicart.helper.SuccessResponse;
import com.bicart.service.RoleService;

/**
 * <p>
 * RoleController class is a REST controller class that handles all the role related requests.
 * </p>
 */
@RestController
@RequestMapping("v1/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    /**
     * <p>
     * Adds a new role to the database.
     * </p>
     *
     * @param roleDto The role details to be added.
     * @return {@link SuccessResponse} with {@link HttpStatus} CREATED
     */
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<SuccessResponse> addRole(@RequestBody RoleDto roleDto) {
        roleService.addRole(roleDto);
        return SuccessResponse.setSuccessResponse("Role added Successfully", HttpStatus.CREATED);
    }

    /**
     * <p>
     * Gets all the roles.
     * </p>
     *
     * @return {@link SuccessResponse} containing roles with {@link HttpStatus} OK
     */
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<SuccessResponse> getAllRoles() {
        Set<RoleDto> roles = roleService.getAllRoles();
        return SuccessResponse.setSuccessResponse("Roles fetched Successfully", HttpStatus.OK, Map.of("roles", roles));
    }

    /**
     * <p>
     * Gets a role by its id.
     * </p>
     *
     * @param name for which role is fetched
     * @return {@link SuccessResponse} role with {@link HttpStatus} OK
     */
    @GetMapping("/{name}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<SuccessResponse> getRoleByName(@PathVariable String name) {
        RoleDto role = roleService.getRoleByName(name);
        return SuccessResponse.setSuccessResponse("Role fetched Successfully", HttpStatus.OK, Map.of("role", role));
    }

    /**
     * <p>
     * Deletes a role.
     * </p>
     *
     * @param name for which role is updated
     * @return {@link SuccessResponse} with {@link HttpStatus} NO_CONTENT
     */
    @DeleteMapping("/{name}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<SuccessResponse> removeRole(@PathVariable String name) {
        roleService.deleteRole(name);
        return SuccessResponse.setSuccessResponse("Role deleted Successfully", HttpStatus.NO_CONTENT);
    }
}
