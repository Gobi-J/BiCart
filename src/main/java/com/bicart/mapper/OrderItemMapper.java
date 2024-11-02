package com.bicart.mapper;

import com.bicart.dto.OrderItemDto;
import com.bicart.model.OrderItem;

public class OrderItemMapper {
    public static OrderItem dtoToModel(OrderItemDto orderItem) {
        return OrderItem.builder()
                .id(orderItem.getId())
                .price(orderItem.getPrice())
                .quantity(orderItem.getQuantity())
                .product(ProductMapper.dtoToModel(orderItem.getProduct()))
                .order(OrderMapper.dtoToModel(orderItem.getOrder()))
                .cart(CartMapper.dtoToModel(orderItem.getCart()))
                .build();
    }

    public static OrderItemDto modelToDto(OrderItem orderItem) {
        return OrderItemDto.builder()
                .product(ProductMapper.modelToDto(orderItem.getProduct()))
                .quantity(orderItem.getQuantity())
                .price(orderItem.getPrice())
                .build();
    }
}
