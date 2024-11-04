package com.bicart.service;

import com.bicart.dto.OrderDto;
import com.bicart.helper.CustomException;
import com.bicart.mapper.OrderMapper;
import com.bicart.model.Order;
import com.bicart.repository.OrderRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    private final static Logger logger = LogManager.getLogger(OrderService.class);

    /**
     * <p>
     * Getting orders by user id
     * </p>
     *
     * @param userId user id
     * @param page page number
     * @param size number of orders per page
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
            logger.error(e);
            throw new CustomException("Error while fetching orders");
        }
    }
}
