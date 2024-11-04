package com.bicart.repository;

import com.bicart.dto.OrderDto;
import com.bicart.model.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface OrderRepository extends JpaRepository<Order, String> {
    Set<Order> findByUserIdAndIsDeletedFalse(String userId, Pageable pageable);
}
