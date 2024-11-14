package com.bicart.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bicart.model.Cart;

/**
 * Repository interface for performing CRUD operations on the Cart entity.
 */
public interface CartRepository extends JpaRepository<Cart, String> {
    Cart findByUserId(String userId);
}
