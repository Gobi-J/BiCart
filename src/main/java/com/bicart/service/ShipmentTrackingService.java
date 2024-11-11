package com.bicart.service;

import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.bicart.constant.ShipmentStatus;
import com.bicart.dto.ShipmentTrackingDto;
import com.bicart.helper.CustomException;
import com.bicart.mapper.ShipmentTrackingMapper;
import com.bicart.model.ShipmentTracking;
import com.bicart.repository.ShipmentTrackingRepository;

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
     * @param shipmentTracking model.
     */
    public void saveShipmentTracking(ShipmentTracking shipmentTracking) {
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
     * Creates a new ShipmentTracking object and saves it in the repository.
     * </p>
     *
     * @param shipmentTrackingDto to create new shipment.
     * @return {@link ShipmentTrackingDto} details which is added
     * @throws CustomException if exception is thrown.
     */
    public ShipmentTrackingDto addShipmentTracking(ShipmentTrackingDto shipmentTrackingDto) {
        ShipmentTracking shipmentTracking = ShipmentTrackingMapper.dtoToModel((shipmentTrackingDto));
        saveShipmentTracking(shipmentTracking);
        shipmentTrackingDto = ShipmentTrackingMapper.modelToDto((shipmentTracking));
        logger.info("ShipmentTracking added successfully with ID: {}", shipmentTrackingDto.getId());
        return shipmentTrackingDto;
    }

    /**
     * <p>
     *  Initializes the shipping of the product.
     * </p>
     *
     * @return {@link ShipmentTracking} initial shipment which is in pending state
     */
    public ShipmentTracking initializeShipping() {
        ShipmentTracking shipmentTracking = ShipmentTracking.builder()
                .id(UUID.randomUUID().toString())
                .location("IN STORE")
                .status(ShipmentStatus.PENDING)
                .build();
        shipmentTracking.setAudit("SYSTEM");
        return shipmentTracking;
    }
}







