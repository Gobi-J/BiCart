package com.bicart.repository;

import com.bicart.model.Role;
import com.bicart.model.User;
import jakarta.validation.constraints.Email;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;


@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Page<User> findAllByIsDeletedFalse(Pageable pageable);

    User findByIdAndIsDeletedFalse(String id);

    User findByEmail(String username);

    boolean existsByEmailAndIsDeletedFalse(@Email String email);

    List<User> findByRoleId(String roleId);

    User findByEmailAndIsDeletedFalse(String username);
}

