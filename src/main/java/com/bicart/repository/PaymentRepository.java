package com.bicart.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bicart.model.Payment;

/**
 * Repository interface for performing CRUD operations on the Payment entity.
 */
@Repository
public interface PaymentRepository extends JpaRepository<Payment, String> {

    Page<Payment> findAllByIsDeletedFalse(Pageable pageable);
    Payment findByIdAndIsDeletedFalse(String id);
}
