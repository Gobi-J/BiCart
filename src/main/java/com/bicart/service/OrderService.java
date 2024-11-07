package com.bicart.service;

import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.bicart.constant.OrderStatus;
import com.bicart.dto.OrderDto;
import com.bicart.helper.CustomException;
import com.bicart.mapper.OrderMapper;
import com.bicart.model.Cart;
import com.bicart.model.Order;
import com.bicart.model.Payment;
import com.bicart.repository.OrderItemRepository;
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
    private final OrderItemRepository orderItemRepository;
    private final ShipmentService shipmentService;

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
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Order> orders = orderRepository.findByUserId(userId, pageable);
            return orders.getContent().stream()
                    .map(OrderMapper::modelToDto)
                    .collect(Collectors.toSet());
        } catch (Exception e) {
            logger.error("Error in retrieving the orders with the user id: {}", userId, e);
            throw new CustomException("Could not retrieve the orders with the user id: " + userId);
        }
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
    public Order getOrderById(String userId, String orderId) {
        try {
            Order order = orderRepository.findByIdAndUserId(orderId, userId);
            if (order == null) {
                throw new NoSuchElementException("Order not found with id: " + orderId);
            }
            return order;
        } catch (Exception e) {
            if (e instanceof NoSuchElementException) {
                logger.warn("Order not found with id: {}", orderId);
                throw e;
            }
            logger.error(e);
            throw new CustomException("Could not fetch order with id: " + orderId);
        }
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
        try {
            Cart cart = cartService.getCart(userId);
            final Order order = new Order();
            order.setId(UUID.randomUUID().toString());
            order.setQuantity(cart.getQuantity());
            order.setPrice(cart.getPrice());
            order.setUser(cart.getUser());
            // TODO
//            order.setOrderItems(cart.getOrderItems());
            order.setStatus(OrderStatus.PENDING);
            order.setDeliveryDate(DateUtil.getUpdatedDate(new Date(), 3));
            order.setCreatedAt(new Date());
            order.setIsDeleted(false);
            saveOrder(order);
            return OrderMapper.modelToDto(order);
        } catch (Exception e) {
            logger.error("Could not create order for user id: {}", userId, e);
            throw new CustomException("Could not create order for user id: " + userId);
        }
    }

    /**
     * <p>
     * Cancels an order.
     * </p>
     *
     * @param userId user id
     * @param orderId order id
     * @throws CustomException if error while deleting order
     */
    public void cancelOrder(String userId, String orderId) {
        try {
            Order order = orderRepository.findByIdAndUserId(orderId, userId);
            if (order == null) {
                throw new NoSuchElementException("Order not found with id: " + orderId);
            }
            order.setStatus(OrderStatus.CANCELLED);
            orderRepository.save(order);
        } catch (Exception e) {
            if (e instanceof NoSuchElementException) {
                logger.warn("Order not found with id: {}", orderId);
                throw e;
            }
            logger.error("Could not cancel order with id: {}", orderId);
            throw new CustomException("Could not cancel order with id: " + orderId);
        }
    }

    public void notifyPayment(String userId, String orderId, Payment payment) {
        Order order = getOrderById(userId, orderId);
        order.setPayment(payment);
        order.setStatus(OrderStatus.PAID);
        cartService.deleteCart(order.getUser().getId());
        shipmentService.initializeShipment(order);
        saveOrder(order);
    }
}