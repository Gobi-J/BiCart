package com.bicart.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CartDto {

    @JsonIgnore
    private String id;

    @NotBlank(message = "Cart must specify its quantity")
    private int quantity;

    @NotBlank(message = "Cart must specify its price")
    private long price;
    
    @JsonIgnore
    private UserDto user;

    private Set<OrderItemDto> orderItems;
}
