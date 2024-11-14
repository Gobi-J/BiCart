package com.bicart.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bicart.model.ShipmentTracking;

/**
 * Repository interface for performing CRUD operations on the ShipmentTracking entity.
 */
@Repository
public interface ShipmentTrackingRepository extends JpaRepository<ShipmentTracking, String> {
    ShipmentTracking findByIdAndIsDeletedFalse(String id);
    Page<ShipmentTracking> findAllByIsDeletedFalse(Pageable pageable);
}
