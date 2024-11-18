package com.bicart.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bicart.dto.PaymentDto;
import com.bicart.helper.SuccessResponse;
import com.bicart.service.PaymentService;

/**
 * <p>
 *     PaymentController class is a REST controller class that handles all the payment related requests.
 * </p>
 */
@RestController
@RequestMapping("v1/orders/{orderId}/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    /**
     * <p>
     *     Creates a new payment for an order.
     * </p>
     * @param userId The user id for which the payment is to be created.
     * @param orderId The order id for which the payment is to be created.
     * @param payment The payment details to be added.
     * @return {@link SuccessResponse} with {@link HttpStatus} CREATED
     */
    @PostMapping
    public ResponseEntity<SuccessResponse> createPayment(@Validated @RequestAttribute("id") String userId, @PathVariable String orderId,
                                                         @RequestBody PaymentDto payment) {
        paymentService.createPayment(userId, orderId, payment);
        return SuccessResponse.setSuccessResponseCreated("Payment Completed Successfully!", null);
    }
}