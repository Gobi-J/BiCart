package com.bicart.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SubCategoryDto {
    private String id;

    @NotBlank(message = "Sub category must have a name")
    private String name;
    private String description;

    private CategoryDto category;
}