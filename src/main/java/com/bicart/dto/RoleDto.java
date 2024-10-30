package com.bicart.dto;

import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RoleDto {

    private String id;

    @NotBlank(message = "Role is mandatory")
    @Pattern(regexp = "^[a-zA-Z]+")
    private String roleName;

    private String description;
}
