package com.bicart.repository;

import java.util.List;

import jakarta.validation.constraints.Email;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bicart.model.User;

/**
 * Repository interface for performing CRUD operations on the User entity.
 */
@Repository
public interface UserRepository extends JpaRepository<User, String> {

    List<User> findAllByIsDeletedFalse(Pageable pageable);

    User findByIdAndIsDeletedFalse(String id);

    User findByEmailAndIsDeletedFalse(String username);

    boolean existsByEmailOrMobileNumber(@Email String email, String mobileNumber);
}

