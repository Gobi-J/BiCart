package com.bicart.repository;

import com.bicart.model.User;
import jakarta.validation.constraints.Email;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Page<User> findAllByIsDeletedFalse(Pageable pageable);

    User findByIdAndIsDeletedFalse(String id);

    User findByEmail(String username);

    boolean existsByEmailAndIsDeletedFalse(@Email String email);
}

