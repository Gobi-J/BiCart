package com.bicart.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {

    @JsonIgnore
    private String id;
    @NotBlank(message = "Category must have a unique name")
    private String name;

    @JsonIgnore
    private String description;
}
