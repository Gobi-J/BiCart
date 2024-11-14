package com.bicart.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bicart.model.Address;

/**
 * Repository interface for performing CRUD operations on the Address entity.
 */
@Repository
public interface AddressRepository extends JpaRepository<Address, String> {
    Address findByIdAndIsDeletedFalse(String id);

    List<Address> findAllByIsDeletedFalse(Pageable pageable);

    List<Address> findByUserIdAndIsDeletedFalse(String userId);

    List<Address> findAllByUserIdAndIsDeletedFalse(String userId, Pageable pageable);
}
