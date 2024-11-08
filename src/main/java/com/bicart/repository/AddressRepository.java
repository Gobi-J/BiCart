package com.bicart.repository;

import java.util.List;
import java.util.Set;

import com.bicart.model.Address;
import com.bicart.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for performing CRUD operations on the Address entity.
 */
@Repository
public interface AddressRepository extends JpaRepository<Address, String> {
    Address findByIdAndIsDeletedFalse(String id);
    // Address findByEmployeeIdAndIsDeletedFalse(String employeeId);
    Page<Address> findAllByIsDeletedFalse(Pageable pageable);

    List<Address> findByUserIdAndIsDeletedFalse(String userId);

    Page<Address> findAllByUserIdAndIsDeletedFalse(String userId, Pageable pageable);
}
