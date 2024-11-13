package com.bicart.repository;

import com.bicart.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, String> {
    List<Order> findByUserId(String userId, Pageable pageable);

    Order findByIdAndUserId(String orderId, String userId);
}
