package com.bicart.repository;

import com.bicart.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * Repository interface for performing CRUD operations on the Role entity.
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
    Role findByIdAndIsDeletedFalse(String id);
    Set<Role> findAllByIsDeletedFalse();

    Role findByRoleNameAndIsDeletedFalse(String name);
}
