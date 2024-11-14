package com.bicart.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class RoleDto {
    @JsonIgnore
    private String id;

    @NotBlank(message = "Role is mandatory")
    @Pattern(regexp = "^[a-zA-Z]+")
    private String roleName;
    @JsonIgnore
    private String description;
}
