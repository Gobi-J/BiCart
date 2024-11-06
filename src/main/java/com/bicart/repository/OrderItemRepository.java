package com.bicart.repository;

import com.bicart.dto.OrderItemDto;
import com.bicart.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface OrderItemRepository extends JpaRepository<OrderItem, String> {
    Set<OrderItemDto> findByOrderId(String orderId);

    Set<OrderItemDto> findByCartId(String cartId);
}
