package com.bicart.dto;

import java.util.Date;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import com.bicart.constant.OrderStatus;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    private String id;

    @NonNull
    private Date deliveryDate;

    @NonNull
    private OrderStatus status;

    @NotBlank(message = "Order must specify its quantity")
    private int quantity;

    @NotBlank(message = "Order must specify its price")
    private long price;
    @JsonIgnore
    private PaymentDto payment;
    @JsonIgnore
    private UserDto user;
    private AddressDto address;

    private Set<OrderItemDto> orderItems;
    private ShipmentDto shipment;
}
