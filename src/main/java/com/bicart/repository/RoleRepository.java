package com.bicart.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bicart.model.Role;

/**
 * Repository interface for performing CRUD operations on the Role entity.
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
    Role findByIdAndIsDeletedFalse(String id);
    List<Role> findAllByIsDeletedFalse();

    Role findByRoleNameAndIsDeletedFalse(String name);
}
