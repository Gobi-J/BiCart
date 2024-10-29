package com.bicart.mapper;

import com.bicart.dto.CartDto;
import com.bicart.model.Cart;

public class CartMapper {
    public static CartDto modeltoDto(Cart cart) {
        return CartDto.builder()
                .id(cart.getId())
                .quantity(cart.getQuantity())
                .price(cart.getPrice())
                .build();
    }

    public static Cart dtotomodel(CartDto cartDto) {
        return Cart.builder()
                .id(cartDto.getId())
                .quantity(cartDto.getQuantity())
                .price(cartDto.getPrice())
                .build();
    }
}
