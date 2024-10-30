package com.bicart.repository;

import com.bicart.model.Role;
import com.bicart.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for performing CRUD operations on the Role entity.
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
    Role findByIdAndIsDeletedFalse(String id);
    Page<Role> findAllByIsDeletedFalse(Pageable pageable);
}
