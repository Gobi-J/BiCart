package com.bicart.controller;

import com.bicart.dto.PaymentDto;
import com.bicart.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
     * @param orderId The order id for which the payment is to be created.
     * @param paymentDto The payment details to be added.
     * @return {@link ResponseEntity<PaymentDto>} payment details that were added with {@link HttpStatus} CREATED
     */
    @PostMapping
    public ResponseEntity<PaymentDto> createPayment(@PathVariable String orderId,
                                                    @RequestBody PaymentDto paymentDto) {
        return new ResponseEntity<>(paymentService.createPayment(orderId, paymentDto), HttpStatus.CREATED);
    }
}