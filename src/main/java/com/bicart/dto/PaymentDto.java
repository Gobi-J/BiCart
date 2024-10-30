package com.bicart.dto;

import com.bicart.constant.PaymentStatus;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

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
