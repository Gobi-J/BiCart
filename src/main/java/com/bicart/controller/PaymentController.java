package com.bicart.controller;

import com.bicart.helper.SuccessResponse;
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
     * @param orderId The order id for which the payment is to be created.
     * @param paymentDto The payment details to be added.
     * @return {@link ResponseEntity<String>} notify success state to user that were added with {@link HttpStatus} CREATED
     */
    @PostMapping
    public ResponseEntity<SuccessResponse> createPayment(@Validated @RequestAttribute("id") String userId, @PathVariable String orderId,
                                                         @RequestBody PaymentDto paymentDto) {
        paymentService.createPayment(userId, orderId, paymentDto);
        return SuccessResponse.setSuccessResponse("Payment Completed Successfully!", HttpStatus.CREATED);
    }
}