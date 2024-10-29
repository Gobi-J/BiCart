package com.bicart.dto;

import com.bicart.constant.OrderStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.Date;

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
    private PaymentDto payment;
    private UserDto user;
}
