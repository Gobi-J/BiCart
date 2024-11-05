package com.bicart.controller;

import com.bicart.dto.PaymentDto;
import com.bicart.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
@RestController
@RequestMapping("v1/payments")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @PostMapping("/{orderId}/payments")
    public ResponseEntity<PaymentDto> addPayment(@PathVariable String orderId, @RequestBody PaymentDto paymentDto) {
        return new ResponseEntity<>(paymentService.addPayment(paymentDto, orderId), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Set<PaymentDto>> getAllPayments(@RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "10") int size) {
        return new ResponseEntity<>(paymentService.getAllPayments(page, size), HttpStatus.OK);
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentDto> getPaymentById(@PathVariable String paymentId){
        return new ResponseEntity<>(paymentService.getPaymentById(paymentId), HttpStatus.OK);
    }

}
