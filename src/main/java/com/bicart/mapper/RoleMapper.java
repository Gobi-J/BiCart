package com.bicart.mapper;

import com.bicart.dto.RoleDto;
import com.bicart.model.Role;

public class RoleMapper {

    public static RoleDto modelToDto(Role role) {
        return RoleDto.builder()
                .roleName(role.getRoleName())
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
