package com.bicart.service;

import java.util.*;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.bicart.constant.OrderStatus;
import com.bicart.dto.OrderDto;
import com.bicart.helper.CustomException;
import com.bicart.mapper.OrderMapper;
import com.bicart.model.Address;
import com.bicart.model.Cart;
import com.bicart.model.Order;
import com.bicart.model.Payment;
import com.bicart.model.User;
import com.bicart.repository.OrderRepository;
import com.bicart.util.DateUtil;

/**
 * <p>
 * Service class that handles business logic related to order.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartService cartService;

    private final static Logger logger = LogManager.getLogger(OrderService.class);
    private final ShipmentService shipmentService;
    private final AddressService addressService;

    /**
     * <p>
     * Saves an order.
     * </p>
     *
     * @param order order to save
     * @throws CustomException if error while saving order
     */
    public void saveOrder(Order order) {
        try {
            orderRepository.save(order);
            logger.info("Order saved successfully with id: {}", order.getId());
        } catch (Exception e) {
            logger.error("Error saving order", e);
            throw new CustomException("Could not save order with id: " + order.getId(), e);
        }
    }


    /**
     * <p>
     * Getting orders by user id
     * </p>
     *
     * @param userId user id
     * @param page   page number
     * @param size   number of orders per page
     * @return {@link Set<OrderDto>} set of orders
     * @throws CustomException if error while fetching orders
     */
    public Set<OrderDto> getOrdersByUserId(String userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<Order> orders = orderRepository.findByUserId(userId, pageable);
        logger.info("Fetched orders for user id: {}", userId);
        return orders.stream()
                .map(OrderMapper::modelToDto)
                .collect(Collectors.toSet());
    }

    /**
     * <p>
     * Getting order by order id
     * </p>
     *
     * @param orderId order id
     * @return {@link Order} order
     * @throws CustomException if error while fetching order
     */
    public OrderDto getOrderById(String userId, String orderId) {
        Order order = getOrderModelById(userId, orderId);
        return OrderMapper.modelToDto(order);
    }

    /**
     * <p>
     * Getting order by order id
     * </p>
     *
     * @param orderId order id
     * @return {@link Order} order
     * @throws CustomException if error while fetching order
     */
    protected Order getOrderModelById(String userId, String orderId) {
        Order order = orderRepository.findByIdAndUserId(orderId, userId);
        if (order == null) {
            logger.warn("Order not found with id: {}", orderId);
            throw new NoSuchElementException("Order not found with id: " + orderId);
        }
        return order;
    }

    /**
     * <p>
     * Creating an order.
     * Transfer the cart to order.
     * </p>
     *
     * @param userId user id
     * @return {@link OrderDto} order
     */
    public OrderDto createOrder(String userId) {
        Cart cart = cartService.getCart(userId);
        if (cart == null) {
            throw new NoSuchElementException("Cart not found for user id: " + userId);
        }
        Order order = Order.builder()
                .id(UUID.randomUUID().toString())
                .quantity(cart.getQuantity())
                .price(cart.getPrice())
                .status(OrderStatus.PENDING)
                .deliveryDate(DateUtil.getUpdatedDate(new Date(), 3))
                .user(cart.getUser())
                .build();
        Address address = addressService.getAddressModelByUserId(userId);
        order.setAddress(address);
        order.setOrderItems(cart.getOrderItems());
        order.setAudit(userId);
        saveOrder(order);
        return OrderMapper.modelToDto(order);
    }

    /**
     * <p>
     * Cancels an order.
     * </p>
     *
     * @param userId  user id
     * @param orderId order id
     * @throws CustomException if error while deleting order
     */
    public void cancelOrder(String userId, String orderId) {
        Order order = getOrderModelById(userId, orderId);
        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new DuplicateKeyException("Order already cancelled");
        }
        order.setStatus(OrderStatus.CANCELLED);
        shipmentService.cancelShipment(order);
        saveOrder(order);
    }

    /**
     * <p>
     * Notifies payment for an order.
     * </p>
     *
     * @param userId  user id
     * @param orderId order id
     * @param payment payment details
     * @throws CustomException if error while updating order
     */
    public void notifyPayment(String userId, String orderId, Payment payment) {
        Order order = getOrderModelById(userId, orderId);
        order.setPayment(payment);
        order.setStatus(OrderStatus.PAID);
        cartService.deleteCart(order.getUser().getId());
        shipmentService.initializeShipment(order);
        saveOrder(order);
    }
}