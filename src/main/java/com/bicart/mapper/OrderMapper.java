package com.bicart.mapper;

import java.util.stream.Collectors;

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
                .user(UserMapper.dtoToModel(order.getUser()))
                .build();
    }

    public static OrderDto modelToDto(Order order) {
        return OrderDto.builder()
                .id(order.getId())
                .deliveryDate(order.getDeliveryDate())
                .price(order.getPrice())
                .quantity(order.getQuantity())
                .status(order.getStatus())
                .address(order.getAddress() != null ? AddressMapper.modelToDto(order.getAddress()) : null)
                .shipment(order.getShipment() != null ? ShipmentMapper.modelToDto(order.getShipment()) : null)
                .orderItems(order.getOrderItems() != null? order.getOrderItems().stream()
                        .map(OrderItemMapper::modelToDto)
                        .collect(Collectors.toSet()) : null)
                .build();
    }
}
