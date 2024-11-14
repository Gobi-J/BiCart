package com.bicart.dto;

import java.util.Set;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDto {

    private String id;

    @NotBlank(message = "Name is mandatory")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Name should contain only alphabets")
    private String name;

    @NotBlank(message = "MobileNumber is mandatory")
    @Pattern(regexp = "^[1-9][0-9]{9}$", message = "Mobile number should contain only numbers")
    private String mobileNumber;

    @Email(message = "Email should be in valid format(Eg. user@gmail.com)")
    private String email;

    @NotBlank(message = "Password is mandatory")
    @Pattern(regexp = "^[1-9a-zA-Z_@-]{8}", message = "Password should contain 8 characters")
    private String password;

    private Set<RoleDto> role;
}
