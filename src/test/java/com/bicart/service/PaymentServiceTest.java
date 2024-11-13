package com.bicart.service;

import com.bicart.constant.OrderStatus;
import com.bicart.constant.PaymentStatus;
import com.bicart.dto.CategoryDto;
import com.bicart.dto.PaymentDto;
import com.bicart.model.Category;
import com.bicart.model.Order;
import com.bicart.model.Payment;
import com.bicart.repository.CategoryRepository;
import com.bicart.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DuplicateKeyException;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @InjectMocks
    private PaymentService paymentService;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private OrderService orderService;

    private Payment payment;
    private PaymentDto paymentDto;
    private Order order;

    @BeforeEach
    void setUp() {
        order = Order.builder()
                .id("1")
                .quantity(2)
                .status(OrderStatus.PENDING)
                .build();
        payment = Payment.builder()
                .id("1")
                .paymentMode("testUPI")
                .price(100)
                .status(PaymentStatus.PENDING)
                .build();
        paymentDto = PaymentDto.builder()
                .id("1")
                .paymentMode("testUPI")
                .price(100)
                .status(PaymentStatus.PENDING)
                .build();
    }


    @Test
    void testGetPaymentByIdSuccess() {
        when(paymentRepository.findByIdAndIsDeletedFalse(anyString())).thenReturn(payment);
        PaymentDto result = paymentService.getPaymentById("1");
        assertEquals(result.getStatus(), payment.getStatus());
    }

    @Test
    void testGetPaymentByIdThrowsNoSuchElementException() {
        when(paymentRepository.findByIdAndIsDeletedFalse(anyString())).thenReturn(null);
        assertThrows(NoSuchElementException.class, () -> paymentService.getPaymentById("1"));
    }

    @Test
    void testCreatePaymentSuccess() {
        when(orderService.getOrderModelById(anyString(), anyString())).thenReturn(order);
        doNothing().when(orderService).notifyPayment(anyString(), anyString(), any());
        paymentService.createPayment("1","1",paymentDto);
    }

    @Test
    void testCreatePaymentThrowsNoSuchElementException() {
        when(orderService.getOrderModelById(anyString(), anyString())).thenReturn(null);
        assertThrows(NoSuchElementException.class, () -> paymentService.createPayment("1","1",paymentDto));
    }
    @Test
    void testCreatePaymentThrowsDuplicateKeyException() {
        order.setStatus(OrderStatus.CANCELLED);
        when(orderService.getOrderModelById(anyString(), anyString())).thenReturn(order);
        assertThrows(DuplicateKeyException.class, () -> paymentService.createPayment("1","1",paymentDto));
    }
}