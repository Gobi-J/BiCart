package com.bicart.mapper;

import java.util.stream.Collectors;

import com.bicart.dto.CartDto;
import com.bicart.model.Cart;

public class CartMapper {
    public static CartDto modelToDto(Cart cart) {
        return CartDto.builder()
                .id(cart.getId())
                .quantity(cart.getQuantity())
                .price(cart.getPrice())
                .orderItems(cart.getOrderItems().stream()
                        .map(OrderItemMapper::modelToDto)
                        .collect(Collectors.toSet()))
                .build();
    }

    public static Cart dtoToModel(CartDto cartDto) {
        return Cart.builder()
                .id(cartDto.getId())
                .quantity(cartDto.getQuantity())
                .price(cartDto.getPrice())
                .build();
    }
}
