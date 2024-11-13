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
public class OrderItemDto {
    @JsonIgnore
    private String id;

    @NotBlank(message = "Item must have its quantity")
    private int quantity;
    @NotBlank(message = "Item must have its price")
    private long price;
    @JsonIgnore
    private CartDto cart;
    @JsonIgnore
    private OrderDto order;
    private OrderProductDto product;
}
