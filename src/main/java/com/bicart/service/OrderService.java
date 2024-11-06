package com.bicart.service;

import com.bicart.constant.OrderStatus;
import com.bicart.dto.OrderDto;
import com.bicart.helper.CustomException;
import com.bicart.mapper.OrderMapper;
import com.bicart.model.Cart;
import com.bicart.model.Order;
import com.bicart.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import com.bicart.util.DateUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * Service class that handles business logic related to order.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    private final static Logger logger = LogManager.getLogger(OrderService.class);
    @Autowired
    private CartService cartService;

    /**
     * <p>
     * Saves an order.
     * </p>
     *
     * @param order order to save
     * @return {@link Order} saved order
     * @throws CustomException if error while saving order
     */
    public Order saveOrder(Order order) {
        try {
            return orderRepository.save(order);
        } catch (Exception e) {
            logger.error("Error saving order", e);
            throw new CustomException("Error saving order");
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
            Page<Order> orders = orderRepository.findByUserIdAndIsDeletedFalse(userId, pageable);
            return orders.getContent().stream()
                    .map(OrderMapper::modelToDto)
                    .collect(Collectors.toSet());
        } catch (Exception e) {
            logger.error("Error in retrieving the orders with the user id: {}", userId, e);
            throw new CustomException("Error while fetching orders");
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
    public Order getOrderById(String orderId) {
        try {
            Order order = orderRepository.findById(orderId).orElse(null);
            if (order == null) {
                throw new NoSuchElementException("Order not found with id: " + orderId);
            }
            return order;
        } catch (Exception e) {
            if (e instanceof NoSuchElementException) {
                throw e;
            }
            logger.error(e);
            throw new CustomException("Error while fetching order");
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
            Order order = new Order();
            order.setQuantity(cart.getQuantity());
            order.setPrice(cart.getPrice());
            order.setUser(cart.getUser());
            order.setOrderItems(cart.getOrderItems());
            order.setStatus(OrderStatus.PENDING);
            order.setDeliveryDate(DateUtil.getUpdatedDate(new Date(), 3));
            order.setCreatedAt(new Date());
            order = saveOrder(order);
            return OrderMapper.modelToDto(order);
        } catch (Exception e) {
            logger.error(e);
            throw new CustomException("Error while creating order");
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
                throw e;
            }
            logger.error(e);
            throw new CustomException("Error while deleting order");
        }
    }

}
