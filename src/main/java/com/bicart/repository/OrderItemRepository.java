package com.bicart.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bicart.model.OrderItem;

/**
 * Repository interface for performing CRUD operations on the OrderItem entity.
 */
public interface OrderItemRepository extends JpaRepository<OrderItem, String> {
}
