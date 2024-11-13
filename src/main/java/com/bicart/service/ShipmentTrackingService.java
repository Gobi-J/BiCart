package com.bicart.service;

import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.bicart.constant.ShipmentStatus;
import com.bicart.helper.CustomException;
import com.bicart.model.ShipmentTracking;
import com.bicart.repository.ShipmentTrackingRepository;

/**
 * <p>
 * Service class that handles business logic related to shipment tracking.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class ShipmentTrackingService {

    private final ShipmentTrackingRepository shipmentTrackingRepository;

    private static final Logger logger = LogManager.getLogger(ShipmentTrackingService.class);

    /**
     * <p>
     * Saves a ShipmentTracking.
     * </p>
     *
     * @param shipmentTracking details of the shipment tracking to save
     */
    private void saveShipmentTracking(ShipmentTracking shipmentTracking) {
        try {
            shipmentTrackingRepository.save(shipmentTracking);
            logger.info("ShipmentTracking saved successfully with the id: {} ", shipmentTracking.getId());
        } catch (Exception e) {
            logger.error("Error in saving shipmentTracking with the id: {} ", shipmentTracking.getId());
            throw new CustomException("Cannot save Shipment Tracking", e);
        }
    }

    /**
     * <p>
     *  Initializes the shipping of the product.
     * </p>
     *
     * @return {@link ShipmentTracking} initial shipment which is in pending state
     */
    protected ShipmentTracking initializeShipping() {
        ShipmentTracking shipmentTracking = ShipmentTracking.builder()
                .id(UUID.randomUUID().toString())
                .location("IN STORE")
                .status(ShipmentStatus.PENDING)
                .build();
        shipmentTracking.setAudit("SYSTEM");
        return shipmentTracking;
    }
}







