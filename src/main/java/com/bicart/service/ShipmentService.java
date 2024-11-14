package com.bicart.service;

import java.util.Set;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.bicart.constant.ShipmentStatus;
import com.bicart.dto.ShipmentDto;
import com.bicart.helper.CustomException;
import com.bicart.mapper.ShipmentMapper;
import com.bicart.model.Order;
import com.bicart.model.Shipment;
import com.bicart.model.ShipmentTracking;
import com.bicart.repository.ShipmentRepository;

/**
 * <p>
 * Service class that handles business logic related to shipment.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class ShipmentService {

    private final ShipmentRepository shipmentRepository;

    private static final Logger logger = LogManager.getLogger(ShipmentService.class);
    private final ShipmentTrackingService shipmentTrackingService;

    /**
     * <p>
     * Saves a shipment to the repository.
     * </p>
     *
     * @param shipment to save.
     * @throws CustomException if any issues occur while saving the shipment.
     */
    private void saveShipment(Shipment shipment) {
        try {
            shipmentRepository.save(shipment);
            logger.info("Shipment saved successfully");
        } catch (Exception e) {
            logger.error("Error in saving shipment", e);
            throw new CustomException("Cannot save shipment");
        }
    }

    /**
     * <p>
     * Creates a new Shipment object and saves it in the repository.
     * </p>
     *
     * @param shipmentDto to create a new shipment.
     * @return {@link ShipmentDto} the newly created shipment.
     */
    public ShipmentDto addShipment(ShipmentDto shipmentDto) {
        Shipment shipment = ShipmentMapper.dtoToModel(shipmentDto);
        shipment.setAudit("SYSTEM");
        saveShipment(shipment);
        shipmentDto = ShipmentMapper.modelToDto(shipment);
        logger.info("Shipment added successfully with ID: {}", shipmentDto.getId());
        return shipmentDto;
    }

    public void initializeShipment(Order order) {
        ShipmentTracking shipmentTracking = shipmentTrackingService.initializeShipping();
        Shipment shipment = Shipment.builder()
                .currentStatus(ShipmentStatus.PENDING)
                .shipmentTracking(Set.of(shipmentTracking))
                .build();
        shipment.setAudit("SYSTEM");
        order.setShipment(shipment);
        saveShipment(shipment);
    }

    /**
     * <p>
     * Cancels the shipment.
     * </p>
     *
     * @param order from which the shipment is to be canceled.
     */
    public void cancelShipment(Order order) {
        Shipment shipment = order.getShipment();
        shipment.setCurrentStatus(ShipmentStatus.CANCELLED);
        saveShipment(shipment);
    }
}







