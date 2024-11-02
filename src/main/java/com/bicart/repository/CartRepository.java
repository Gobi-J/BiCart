package com.bicart.repository;

import com.bicart.dto.CartDto;
import com.bicart.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, String> {
    Cart findByUserId(String userId);
}
