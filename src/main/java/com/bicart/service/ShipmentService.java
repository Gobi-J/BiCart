package com.bicart.service;

import com.bicart.dto.ShipmentDto;
import com.bicart.helper.CustomException;

import com.bicart.mapper.ShipmentMapper;
import com.bicart.model.Shipment;
import com.bicart.repository.ShipmentRepository;
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
public class ShipmentService {

    @Autowired
    private ShipmentRepository shipmentRepository;

    private static final Logger logger = LogManager.getLogger(ShipmentService.class);

    /**
     * <p>
     * Saves a Shipment.
     * </p>
     *
     * @param shipment model.
     */
    public void saveShipment(Shipment shipment) {
        try {
            shipmentRepository.save(shipment);
            logger.info("Shipment saved successfully");
        } catch (Exception e) {
            logger.error("Error in saving shipment");
            throw new CustomException("Server error!!", e);
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
    public ShipmentDto addShipment(ShipmentDto shipmentDto) throws CustomException {
        try {
            Shipment shipment = ShipmentMapper.dtoToModel((shipmentDto));
            shipmentRepository.save(shipment);
            ShipmentDto shipmentDto1 = ShipmentMapper.modelToDto((shipment));
            logger.info("Shipment added successfully with ID: {}", shipmentDto1.getId());
            return shipmentDto1;
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
    public Set<ShipmentDto> getAllShipments(int page, int size) throws CustomException {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Shipment> shipmentPage = shipmentRepository.findAllByIsDeletedFalse(pageable);
            logger.info("Displayed shipment details for page : {}", page);
            return shipmentPage.getContent().stream()
                    .map(ShipmentMapper::modelToDto)
                    .collect(Collectors.toSet());
        } catch (Exception e) {
            logger.error("Error in retrieving all shipment", e);
            throw new CustomException("Server Error!!!!", e);
        }
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
    public ShipmentDto getShipmentById(String id) throws NoSuchElementException, CustomException {
        try {
            Shipment shipment = shipmentRepository.findByIdAndIsDeletedFalse(id);
            if (shipment == null) {
                throw new NoSuchElementException("Shipment not found for the given id: " + id);
            }
            logger.info("Retrieved shipment details for ID: {}", id);
            return ShipmentMapper.modelToDto(shipment);
        } catch (NoSuchElementException e) {
            logger.error("Shipment not found", e);
            throw e;
        } catch (Exception e) {
            logger.error("Error in retrieving a shipment : {}", id, e);
            throw new CustomException("Server error!!", e);
        }
    }
}







