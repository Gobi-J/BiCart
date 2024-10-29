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
public class CategoryDto {
    private String id;
    @NotBlank(message = "Category must have a unique name")
    private String name;
    private String description;
}
