package com.bicart.mapper;

import com.bicart.dto.RoleDto;
import com.bicart.model.Role;

public class RoleMapper {

    public static RoleDto modelToDto(Role role) {
        return RoleDto.builder()
                .id(role.getId())
                .roleName(role.getRoleName())
                .description(role.getDescription())
                .build();
    }

    public static Role dtoToModel(RoleDto roleDto) {
        return Role.builder()
                .id(roleDto.getId())
                .roleName(roleDto.getRoleName())
                .description(roleDto.getDescription())
                .build();
    }
}
