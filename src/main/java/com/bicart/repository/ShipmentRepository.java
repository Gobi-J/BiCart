package com.bicart.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bicart.model.Shipment;

/**
 * Repository interface for performing CRUD operations on the Shipment entity.
 */
@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, String> {
     Shipment findByIdAndIsDeletedFalse(String id);
     Page<Shipment> findAllByIsDeletedFalse(Pageable pageable);
}
