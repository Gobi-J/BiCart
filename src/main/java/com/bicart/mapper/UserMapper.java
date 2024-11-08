package com.bicart.mapper;

import java.util.stream.Collectors;

import com.bicart.dto.UserDto;
import com.bicart.model.User;

public class UserMapper {

    public static UserDto modelToDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .mobileNumber(user.getMobileNumber())
                .email(user.getEmail())
                .role((user.getRole() == null) ? null : user.getRole().stream()
                        .map(RoleMapper::modelToDto)
                        .collect(Collectors.toSet()))
                .build();
    }

    public static UserDto modelToResponseUserDto(User user) {
        return UserDto.builder()
                .name(user.getName())
                .mobileNumber(user.getMobileNumber())
                .email(user.getEmail())
                .build();
    }

    public static UserDto modelToUserRoleDto(User user) {
        return UserDto.builder()
                .name(user.getName())
                .mobileNumber(user.getMobileNumber())
                .email(user.getEmail())
                .role((user.getRole() == null) ? null : user.getRole().stream()
                        .map(RoleMapper::modelToDto)
                        .collect(Collectors.toSet()))
                .build();
    }

    public static User dtoToModel(UserDto userDto) {
        return User.builder()
                .id(userDto.getId())
                .name(userDto.getName())
                .email(userDto.getEmail())
                .mobileNumber(userDto.getMobileNumber())
                .role((userDto.getRole() == null) ? null : userDto.getRole().stream()
                        .map(RoleMapper::dtoToModel)
                        .collect(Collectors.toSet()))
                .build();
    }
}

