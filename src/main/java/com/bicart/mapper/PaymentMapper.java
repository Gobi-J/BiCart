package com.bicart.mapper;

import com.bicart.dto.PaymentDto;
import com.bicart.model.Payment;

public class PaymentMapper {
    public static PaymentDto modelToDto(Payment payment) {
        return PaymentDto.builder()
                .id(payment.getId())
                .paymentMode(payment.getPaymentMode())
                .price(payment.getPrice())
                .status(payment.getStatus())
                .build();
    }

    public static Payment dtoToModel(PaymentDto paymentDto) {
        return Payment.builder()
                .id(paymentDto.getId())
                .paymentMode(paymentDto.getPaymentMode())
                .price(paymentDto.getPrice())
                .status(paymentDto.getStatus())
                .build();
    }
}
