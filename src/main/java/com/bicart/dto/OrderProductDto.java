package com.bicart.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderProductDto {
    private String id;

    @NotBlank(message = "Product name should not be blank")
    private String name;
    private String description;

    @NotBlank(message = "Product must have price")
    private long price;

    @JsonIgnore
    private int quantity;

    @JsonIgnore
    private SubCategoryDto subCategory;
}
