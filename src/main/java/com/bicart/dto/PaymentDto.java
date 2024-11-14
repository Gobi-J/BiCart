package com.bicart.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

import com.bicart.constant.PaymentStatus;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PaymentDto {

    private String  id;

    @NotBlank(message = "Payment mode is mandatory")
    private String paymentMode;

    @NotBlank(message = "Price is mandatory")
    private long price;

    private PaymentStatus status;
}
