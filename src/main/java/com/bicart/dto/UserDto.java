package com.bicart.dto;

import com.bicart.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDto {

    private String id;

    @NotBlank(message = "Name is mandatory")
    @Pattern(regexp = "^[a-zA-Z]+")
    private String name;

    @NotBlank(message = "mobileNumber is mandatory")
    @Pattern(regexp = "^[1-9][0-9]{9}$", message = " Mobile number should contain only numbers")
    private String mobileNumber;

    @Email
    private String email;

    @NotBlank(message = "Password is mandatory")
    private String password;

    private Set<RoleDto> roles;
}
