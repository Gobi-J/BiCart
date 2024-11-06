package com.bicart.controller;

import com.bicart.dto.RoleDto;
import com.bicart.model.Role;
import com.bicart.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * <p>
 *     RoleController class is a REST controller class that handles all the role related requests.
 * </p>
 */
@RestController
@RequestMapping("v1/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    /**
     * <p>
     *     Adds a new role to the database.
     * </p>
     * @param roleDto The role details to be added.
     * @return {@link ResponseEntity<RoleDto>} role details that were added with {@link HttpStatus} CREATED
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<RoleDto> addRole(@RequestBody RoleDto roleDto) {
        return new ResponseEntity<>(roleService.addRole(roleDto), HttpStatus.CREATED);
    }

    /**
     * <p>
     *     Gets all the roles.
     * </p>
     * @return {@link ResponseEntity<Set<RoleDto>} roles with {@link HttpStatus} OK
     */
    @GetMapping
    public ResponseEntity<Set<RoleDto>> getAllRoles() {
        return new ResponseEntity<>(roleService.getAllRoles(), HttpStatus.OK);
    }

    /**
     * <p>
     *     Gets a role by its id.
     * </p>
     * @param name for which role is fetched
     * @return {@link ResponseEntity<Role>} role with {@link HttpStatus} OK
     */
    @GetMapping("/{name}")
    public ResponseEntity<Role> getRoleByName(@PathVariable String name){
        return new ResponseEntity<>(roleService.getRoleByName(name), HttpStatus.OK);
    }

    /**
     * <p>
     *     Deletes a role.
     * </p>
     * @param name for which role is updated
     * @return {@link HttpStatus} NO_CONTENT if role is deleted
     */
    @DeleteMapping("/{name}")
    public ResponseEntity<String> removeRole(@PathVariable String name) {
        roleService.deleteRole(name);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
