package com.bicart.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.bicart.model.Order;

/**
 * Repository interface for performing CRUD operations on the Order entity.
 */
public interface OrderRepository extends JpaRepository<Order, String> {
    List<Order> findByUserId(String userId, Pageable pageable);

    Order findByIdAndUserId(String orderId, String userId);
}
