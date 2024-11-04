package com.bicart.service;

import com.bicart.dto.ShipmentTrackingDto;
import com.bicart.helper.CustomException;
import com.bicart.mapper.ShipmentTrackingMapper;
import com.bicart.model.ShipmentTracking;
import com.bicart.repository.ShipmentTrackingRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ShipmentTrackingService {

    @Autowired
    private ShipmentTrackingRepository shipmentTrackingRepository;

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
            logger.info("ShipmentTracking saved successfully");
        } catch (Exception e) {
            logger.error("Error in saving shipmentTracking");
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
    public ShipmentTrackingDto addShipmentTracking(ShipmentTrackingDto shipmentTrackingDto) throws CustomException {
        try {
            ShipmentTracking shipmentTracking = ShipmentTrackingMapper.dtoToModel((shipmentTrackingDto));
            shipmentTrackingRepository.save(shipmentTracking);
            ShipmentTrackingDto shipmentTrackingDto1 = ShipmentTrackingMapper.modelToDto((shipmentTracking));
            logger.info("ShipmentTracking added successfully with ID: {}", shipmentTrackingDto1.getId());
            return shipmentTrackingDto1;
        } catch (Exception e) {
            logger.error("Error adding a shipment", e);
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
    public Set<ShipmentTrackingDto> getAllShipmentTrackings(int page, int size) throws CustomException {
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
     * Retrieves and displays the details of an shipmentTrackings.
     * </p>
     *
     * @param id the ID of the ShipmentTracking whose details are to be viewed
     * @return the ShipmentTracking object.
     * @throws NoSuchElementException when occurred.
     */
    public ShipmentTrackingDto getShipmentTrackingById(String id) throws NoSuchElementException, CustomException {
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
}







