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
            throw new CustomException("Server error!!", e);
        }
    }

    /**
     * <p>
     * Creates a new ShipmentTracking object and saves it in the repository.
     * </p>
     *
     * @param shipmentTrackingDto to create new shipment.
     * @return the created shipmentTrackingDto object.
     * @throws CustomException if exception is thrown.
     */
    public ShipmentTrackingDto addShipmentTracking(ShipmentTrackingDto shipmentTrackingDto) {
        try {
            ShipmentTracking shipmentTracking = ShipmentTrackingMapper.dtoToModel((shipmentTrackingDto));
            saveShipmentTracking(shipmentTracking);
            shipmentTrackingDto = ShipmentTrackingMapper.modelToDto((shipmentTracking));
            logger.info("ShipmentTracking added successfully with ID: {}", shipmentTrackingDto.getId());
            return shipmentTrackingDto;
        } catch (Exception e) {
            logger.error("Error adding a shipment with ID: {}", shipmentTrackingDto.getId(), e);
            throw new CustomException("Server Error!!!!", e);
        }
    }

    /**
     * <p>
     * Retrieves and displays all shipment.
     * </p>
     *
     * @return {@link Set <ShipmentDto>} all the Shipment.
     * @throws CustomException, when any custom Exception is thrown.
     */
    public Set<ShipmentTrackingDto> getAllShipmentTracking(int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<ShipmentTracking> shipmentTrackingPage = shipmentTrackingRepository.findAllByIsDeletedFalse(pageable);
            logger.info("Displayed shipmentTracking details for page : {}", page);
            return shipmentTrackingPage.getContent().stream()
                    .map(ShipmentTrackingMapper::modelToDto)
                    .collect(Collectors.toSet());
        } catch (Exception e) {
            logger.error("Error in retrieving all shipmentTracking", e);
            throw new CustomException("Server Error!!!!", e);
        }
    }

    /**
     * <p>
     * Retrieves and displays the details of an shipmentTracking.
     * </p>
     *
     * @param id the ID of the ShipmentTracking whose details are to be viewed
     * @return the ShipmentTracking object.
     * @throws NoSuchElementException when occurred.
     */
    public ShipmentTrackingDto getShipmentTrackingById(String id) {
        try {
            ShipmentTracking shipmentTracking = shipmentTrackingRepository.findByIdAndIsDeletedFalse(id);
            if (shipmentTracking == null) {
                throw new NoSuchElementException("ShipmentTracking not found for the given id: " + id);
            }
            logger.info("Retrieved shipmentTracking details for ID: {}", id);
            return ShipmentTrackingMapper.modelToDto(shipmentTracking);
        } catch (Exception e) {
            if (e instanceof NoSuchElementException) {
                logger.error("ShipmentTracking not found", e);
                throw e;
            }
            logger.error("Error in retrieving a shipmentTracking : {}", id, e);
            throw new CustomException("Server Error!!!!", e);
        }
    }

    public ShipmentTracking initializeShipping() {
        ShipmentTracking shipmentTracking = new ShipmentTracking();
        shipmentTracking.setId(UUID.randomUUID().toString());
        shipmentTracking.setLocation("IN STORE");
        shipmentTracking.setStatus(ShipmentStatus.PENDING);
        return shipmentTracking;
    }
}







