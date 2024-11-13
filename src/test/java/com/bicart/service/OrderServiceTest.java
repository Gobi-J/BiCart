package com.bicart.service;

import com.bicart.constant.OrderStatus;
import com.bicart.dto.*;
import com.bicart.helper.CustomException;
import com.bicart.model.*;
import com.bicart.repository.CategoryRepository;
import com.bicart.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.PageRequest;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CartService cartService;
    @Mock
    private AddressService addressService;
    @Mock
    private ShipmentService shipmentService;

    private Order order;
    private OrderDto orderDto;
    private Cart cart;
    private Address address;
    private Payment payment;
    private User user;


    @BeforeEach
    void setUp() {
        user = User.builder()
                .id("1")
                .build();
        cart = Cart.builder()
                .id("1")
                .price(100)
                .build();
        order = Order.builder()
                .id("1")
                .quantity(3)
                .price(100)
                .status(OrderStatus.PENDING)
                .deliveryDate(new Date())
                .user(user)
                .payment(payment)
                .build();
        orderDto = OrderDto.builder()
                .id("1")
                .quantity(3)
                .price(100)
                .status(OrderStatus.PENDING)
                .deliveryDate(new Date())
                .build();
    }

    @Test
    void testCreateOrderSuccess() {
        when(cartService.getCart(anyString())).thenReturn(cart);
        when(addressService.getAddressModelByUserId(anyString())).thenReturn(address);
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        orderService.createOrder("1");
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void testCreateOrderThrowsNoSuchElementException() {
        when(cartService.getCart(anyString())).thenReturn(null);
        assertThrows(NoSuchElementException.class, () -> orderService.createOrder("1"));
    }


    @Test
    void testCancelOrderSuccess() {
        when(orderRepository.findByIdAndUserId(anyString(), anyString())).thenReturn(order);
        doNothing().when(shipmentService).cancelShipment(order);
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        orderService.cancelOrder("1", "1");
    }

    @Test
    void testCancelOrderThrowsDuplicateKeyException() {
        when(orderRepository.findByIdAndUserId(anyString(), anyString())).thenReturn(order);
        order.setStatus(OrderStatus.CANCELLED);
        when(orderService.getOrderModelById(anyString(), anyString())).thenReturn(order);
        assertThrows(DuplicateKeyException.class, () -> orderService.cancelOrder("1", "1"));
    }

    @Test
    void testNotifyPaymentSuccess() {
        when(orderRepository.findByIdAndUserId(anyString(), anyString())).thenReturn(order);
        doNothing().when(cartService).deleteCart("1");
        doNothing().when(shipmentService).initializeShipment(order);
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        orderService.notifyPayment("1", "1", payment);
    }

    @Test
    void testGetOrderById() {
        when(orderRepository.findByIdAndUserId(anyString(), anyString())).thenReturn(order);
        orderService.getOrderById("1", "1");
    }

    @Test
    void testSaveOrderThrowsCustomException() {
        when(orderRepository.save(any(Order.class))).thenThrow(CustomException.class);
        assertThrows(CustomException.class, () -> orderService.saveOrder(order));
    }

    @Test
    void testGetOrdersByUserIdSuccess() {
        when(orderRepository.findByUserId("1", PageRequest.of(0, 1))).thenReturn(List.of(order));
        assertEquals(1, orderService.getOrdersByUserId("1", 0, 1).size());
    }

    @Test
    void testGetOrderModelByIdSuccess() {
        when(orderRepository.findByIdAndUserId(anyString(), anyString())).thenReturn(order);
        Order result = orderService.getOrderModelById("1", "1");
        assertNotNull(result);
        assertEquals(result.getQuantity(), order.getQuantity());
    }

    @Test
    void testGetOrderModelByIdThrowsNoSuchElementException() {
        when(orderRepository.findByIdAndUserId(anyString(), anyString())).thenReturn(null);
        assertThrows(NoSuchElementException.class, () -> orderService.getOrderModelById("1", "1"));
    }
}