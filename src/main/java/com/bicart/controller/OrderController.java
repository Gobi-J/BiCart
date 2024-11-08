package com.bicart.controller;

import java.util.Set;

import com.bicart.helper.SuccessResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bicart.dto.OrderDto;
import com.bicart.model.Order;
import com.bicart.service.OrderService;

/**
 * <p>
 * OrderController class is a REST controller class that handles all the requests related to orders.
 * </p>
 */
@RestController
@RequestMapping("/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /**
     * <p>
     * Get all the orders of the user.
     * </p>
     *
     * @param page   Page number
     * @param size   Number of orders per page
     * @param userId fetched from the request attribute
     * @return {@link ResponseEntity<Set<OrderDto>>} containing the list of orders of the user, with HTTP status OK
     */
    @GetMapping
    public ResponseEntity<SuccessResponse> getOrders(@RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "10") int size,
                                                     @RequestAttribute("id") String userId) {
        Set<OrderDto> orderDto = orderService.getOrdersByUserId(userId, page, size);
        return SuccessResponse.setSuccessResponse("Orders Fetched Successfully", HttpStatus.OK, orderDto);
    }

    /**
     * <p>
     * Get the order by id.
     * </p>
     *
     * @param orderId Id of the order
     * @param userId  fetched from the request attribute
     * @return {@link ResponseEntity<Order>} containing the order, with HTTP status OK
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<SuccessResponse> getOrderById(@RequestAttribute("id") String userId, @PathVariable String orderId) {
        OrderDto orderDto = orderService.getOrderById(userId, orderId);
        return SuccessResponse.setSuccessResponse("Order Fetched Successfully", HttpStatus.OK, orderDto);
    }

    /**
     * <p>
     * Create a new order.
     * </p>
     *
     * @param userId fetched from the request attribute
     * @return {@link ResponseEntity<OrderDto>} containing the created order, with HTTP status CREATED
     */
    @PostMapping
    public ResponseEntity<SuccessResponse> createOrder(@Validated @RequestAttribute("id") String userId) {
        OrderDto order = orderService.createOrder(userId);
        return SuccessResponse.setSuccessResponse("Orders Created Successfully", HttpStatus.CREATED, order);
    }

    /**
     * <p>
     * Cancel the order.
     * </p>
     *
     * @param orderId Id of the order
     * @param userId  fetched from the request attribute
     * @return {@link ResponseEntity<HttpStatus>} with {@link HttpStatus} NO_CONTENT
     */
    @DeleteMapping("/{orderId}")
    public ResponseEntity<SuccessResponse> cancelOrder(@RequestAttribute("id") String userId, @PathVariable String orderId) {
        orderService.cancelOrder(userId, orderId);
        return SuccessResponse.setSuccessResponse("Order Cancelled Successfully", HttpStatus.NO_CONTENT);
    }
}
