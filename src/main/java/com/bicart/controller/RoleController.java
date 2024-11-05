package com.bicart.controller;

import com.bicart.dto.RoleDto;
import com.bicart.dto.UserDto;
import com.bicart.model.Role;
import com.bicart.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("v1/roles")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @PostMapping
    public ResponseEntity<RoleDto> addRole(@RequestBody RoleDto roleDto) {
        return new ResponseEntity<>(roleService.addRole(roleDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Set<RoleDto>> getAllRoles() {
        return new ResponseEntity<>(roleService.getAllRoles(), HttpStatus.OK);
    }

    @GetMapping("/{roleId}")
    public ResponseEntity<Role> getRoleById(@PathVariable String roleId){
        return new ResponseEntity<>(roleService.getRoleById(roleId), HttpStatus.OK);
    }

    @DeleteMapping("/{roleId}")
    public ResponseEntity<String> removeRole(@PathVariable String roleId) {
        roleService.deleteRole(roleId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
