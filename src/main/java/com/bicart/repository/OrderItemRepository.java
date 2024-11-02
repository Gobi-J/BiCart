package com.bicart.repository;

import com.bicart.dto.OrderItemDto;
import com.bicart.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, String> {
    List<OrderItemDto> findByOrderId(String orderId);

    List<OrderItemDto> findByCartId(String cartId);
}
