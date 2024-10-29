package com.bicart.mapper;

import com.bicart.dto.OrderDto;
import com.bicart.model.Order;

public class OrderMapper {
    public static Order dtoToModel(OrderDto order) {
        return Order.builder()
                .id(order.getId())
                .deliveryDate(order.getDeliveryDate())
                .price(order.getPrice())
                .quantity(order.getQuantity())
                .status(order.getStatus())
                .payment(PaymentMapper.dtoToModel(order.getPayment()))
                .user(UserMapper.dtoToMapper(order.getUser()))
                .build();
    }

    public static OrderDto modelToDto(Order order) {
        return OrderDto.builder()
                .deliveryDate(order.getDeliveryDate())
                .price(order.getPrice())
                .quantity(order.getQuantity())
                .status(order.getStatus())
                .build();
    }
}
