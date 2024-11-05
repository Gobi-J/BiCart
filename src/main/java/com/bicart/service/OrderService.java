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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    private final static Logger logger = LogManager.getLogger(OrderService.class);
    @Autowired
    private CartService cartService;

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
            Set<Order> orders = orderRepository.findByUserIdAndIsDeletedFalse(userId, pageable);
            return orders.stream()
                    .map(OrderMapper::modelToDto)
                    .collect(Collectors.toSet());
        } catch (Exception e) {
            logger.error("Error in retrieving the orders with the user id: {}", userId, e);
            throw new CustomException("Error while fetching orders");
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
            order = orderRepository.save(order);
            return OrderMapper.modelToDto(order);
        } catch (Exception e) {
            logger.error(e);
            throw new CustomException("Error while creating order");
        }
    }

}
