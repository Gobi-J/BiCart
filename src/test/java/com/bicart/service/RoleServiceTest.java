package com.bicart.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.bicart.dto.RoleDto;
import com.bicart.model.Role;
import com.bicart.repository.RoleRepository;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {

    private Role role;
    private RoleDto roleDto;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleService roleService;

    @BeforeEach
    void setUp() {
        role = Role.builder()
                .roleName("ADMIN")
                .build();

        roleDto = RoleDto.builder()
                .roleName("ADMIN")
                .build();
    }

    @Test
    void testAddRoleSuccess() {
        when(roleRepository.save(any(Role.class))).thenReturn(role);
        roleService.addRole(roleDto);
        verify(roleRepository).save(any(Role.class));
    }

    @Test
    void testGetAllRolesSuccess() {
        when(roleRepository.findAllByIsDeletedFalse()).thenReturn(List.of(role));
        Set<RoleDto> roles = roleService.getAllRoles();
        assertEquals(1, roles.size());
    }

    @Test
    void testGetRoleByNameSuccess() {
        when(roleRepository.findByRoleNameAndIsDeletedFalse(anyString())).thenReturn(role);
        RoleDto result = roleService.getRoleByName("ADMIN");
        assertEquals(result.getRoleName(), roleDto.getRoleName());
    }

    @Test
    void testGetRoleModelByNameSuccess() {
        when(roleRepository.findByRoleNameAndIsDeletedFalse(anyString())).thenReturn(role);
        Role result = roleService.getRoleModelByName("ADMIN");
        assertEquals(result.getRoleName(), role.getRoleName());
    }

    @Test
    void testGetRoleModelByNameNotExists() {
        when(roleRepository.findByRoleNameAndIsDeletedFalse(anyString())).thenReturn(null);
        assertThrows(NoSuchElementException.class, () -> roleService.getRoleModelByName("ADMIN"));
    }

    @Test
    void testDeleteRoleSuccess() {
        when(roleRepository.findByRoleNameAndIsDeletedFalse(anyString())).thenReturn(role);
        when(roleRepository.save(any(Role.class))).thenReturn(role);
        roleService.deleteRole("ADMIN");
        verify(roleRepository).save(any(Role.class));
    }
}