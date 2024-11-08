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
public class ProductDto {

    @NotBlank(message = "Product name should not be blank")
    private String name;
    private String description;

    @NotBlank(message = "Product must have price")
    private long price;

    @NotBlank(message = "Product must have remaining stock")
    private int quantity;
    private SubCategoryDto subCategory;
}
