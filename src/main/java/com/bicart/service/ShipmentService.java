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
import com.bicart.dto.ShipmentDto;
import com.bicart.helper.CustomException;
import com.bicart.mapper.ShipmentMapper;
import com.bicart.model.Order;
import com.bicart.model.Shipment;
import com.bicart.model.ShipmentTracking;
import com.bicart.repository.ShipmentRepository;

@Service
@RequiredArgsConstructor
public class ShipmentService {

    private final ShipmentRepository shipmentRepository;

    private static final Logger logger = LogManager.getLogger(ShipmentService.class);
    private final ShipmentTrackingService shipmentTrackingService;

    /**
     * <p>
     * Saves a Shipment.
     * </p>
     *
     * @param shipment to save.
     */
    public void saveShipment(Shipment shipment) {
        try {
            shipmentRepository.save(shipment);
            logger.info("Shipment saved successfully");
        } catch (Exception e) {
            logger.error("Error in saving shipment");
            throw new CustomException("Cannot save shipment");
        }
    }

    /**
     * <p>
     * Creates a new Shipment object and saves it in the repository.
     * </p>
     *
     * @param shipmentDto to create new shipment.
     * @return the created shipmentDto object.
     * @throws CustomException if exception is thrown.
     */
    public ShipmentDto addShipment(ShipmentDto shipmentDto) {
        Shipment shipment = ShipmentMapper.dtoToModel(shipmentDto);
        shipment.setId(UUID.randomUUID().toString());
        shipment.setAudit("SYSTEM");
        saveShipment(shipment);
        shipmentDto = ShipmentMapper.modelToDto((shipment));
        logger.info("Shipment added successfully with ID: {}", shipmentDto.getId());
        return shipmentDto;
    }

    /**
     * <p>
     * Retrieves and displays all shipment.
     * </p>
     *
     * @return {@link Set <ShipmentDto>} all the Shipment.
     * @throws CustomException, when any custom Exception is thrown.
     */
    public Set<ShipmentDto> getAllShipments(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Shipment> shipmentPage = shipmentRepository.findAllByIsDeletedFalse(pageable);
        logger.info("Displayed shipment details for page : {}", page);
        return shipmentPage.getContent().stream()
                .map(ShipmentMapper::modelToDto)
                .collect(Collectors.toSet());
    }

    /**
     * <p>
     * Retrieves and displays the details of an shipments.
     * </p>
     *
     * @param id the ID of the Shipment whose details are to be viewed
     * @return the Shipment object.
     * @throws NoSuchElementException when occurred.
     */
    public ShipmentDto getShipmentById(String id) {
        Shipment shipment = shipmentRepository.findByIdAndIsDeletedFalse(id);
        if (shipment == null) {
            throw new NoSuchElementException("Shipment not found for the given id: " + id);
        }
        logger.info("Retrieved shipment details for ID: {}", id);
        return ShipmentMapper.modelToDto(shipment);
    }

    public void initializeShipment(Order order) {
        ShipmentTracking shipmentTracking = shipmentTrackingService.initializeShipping();
        Shipment shipment = Shipment.builder()
                .id(UUID.randomUUID().toString())
                .currentStatus(ShipmentStatus.PENDING)
                .shipmentTracking(Set.of(shipmentTracking))
                .build();
        shipment.setAudit("SYSTEM");
        saveShipment(shipment);
        order.setShipment(shipment);
    }

    /**
     * <p>
     * Cancels the shipment.
     * </p>
     *
     * @param order order
     */
    public void cancelShipment(Order order) {
        Shipment shipment = order.getShipment();
        shipment.setCurrentStatus(ShipmentStatus.CANCELLED);
        saveShipment(shipment);
    }
}







