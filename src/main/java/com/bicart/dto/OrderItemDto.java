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
public class OrderItemDto {
    private String id;

    @NotBlank(message = "Item must have its quantity")
    private int quantity;
    @NotBlank(message = "Item must have its price")
    private double price;

    private CartDto cart;
    private OrderDto order;
    private ProductDto product;
}
