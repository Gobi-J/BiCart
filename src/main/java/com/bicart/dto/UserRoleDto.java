package com.bicart.dto;

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
public class UserRoleDto {

    @NotBlank(message = "Name is mandatory")
    @Pattern(regexp = "^[a-zA-Z]+")
    private String name;

    @NotBlank(message = "MobileNumber is mandatory")
    @Pattern(regexp = "^[1-9][0-9]{9}$", message = " Mobile number should contain only numbers")
    private String mobileNumber;

    @Email(message = "Email should be in valid format(Eg. user@gmail.com)")
    private String email;

    private RoleDto role;

}
