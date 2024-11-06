package com.bicart.service;

import com.bicart.dto.AddressDto;
import com.bicart.helper.CustomException;
import com.bicart.mapper.AddressMapper;
import com.bicart.model.Address;
import com.bicart.repository.AddressRepository;
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

/**
 * <p>
 *   Service class that handles business logic related to address.
 * </p>
 */
@Service
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;

    private static final Logger logger = LogManager.getLogger(AddressService.class);

    /**
     * <p>
     * Saves an Address.
     * </p>
     *
     * @param address model.
     */
    public void saveAddress(Address address) {
        try {
            addressRepository.save(address);
            logger.info("Address saved successfully with the id : {} ", address.getId());
        } catch (Exception e) {
            logger.error("Error in saving address with the id : {}", address.getId());
            throw new CustomException("Server error!!", e);
        }
    }

    /**
     * <p>
     * Creates a new Address object and saves it in the repository.
     * </p>
     *
     * @param addressDTO to create new address.
     * @return the created addressDto object.
     * @throws CustomException, DuplicateKeyException if exception is thrown.
     */
    public AddressDto addAddress(AddressDto addressDTO) {
        try {
            Address address = AddressMapper.dtoToModel((addressDTO));
            addressRepository.save(address);
            AddressDto addressDto = AddressMapper.modelToDto((address));
            logger.info("Address added successfully with Id {}", addressDto.getId());
            return addressDto;
        } catch (Exception e) {
            logger.error("Error adding a address with the phone: {}", addressDTO.getPhone(), e);
            throw new CustomException("Server Error!!!!", e);
        }
    }

    /**
     * <p>
     * Retrieves and displays all address.
     * </p>
     *
     * @param page entries from which page are to be fetched
     * @param size number of entries needed
     * @return {@link Set <AddressDto>} all the Address.
     * @throws CustomException, when any custom Exception is thrown.
     */
    public Set<AddressDto> getAllAddresses(int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Address> addressPage = addressRepository.findAllByIsDeletedFalse(pageable);
            logger.info("Displayed address details for page : {}", page);
            return addressPage.getContent().stream()
                    .map(AddressMapper::modelToDto)
                    .collect(Collectors.toSet());
        } catch (Exception e) {
            logger.error("Error in retrieving all addresses", e);
            throw new CustomException("Server Error!!!!", e);
        }
    }

    /**
     * <p>
     * Updates the address with new details.
     * </p>
     *
     * @param addressDto to update the all the details of the address.
     * @return the updated Address object.
     * @throws CustomException when exception is thrown.
     */
    public AddressDto updateAddress(AddressDto addressDto) {
        try {
            Address address = AddressMapper.dtoToModel((addressDto));
            addressRepository.save(address);
            logger.info("Address updated successfully for ID: {}", addressDto.getId());
            return AddressMapper.modelToDto((address));
        } catch (Exception e) {
            logger.error("Error updating address for ID: {}", addressDto.getId(), e);
            throw new CustomException("Server Error!!!!", e);
        }
    }

    /**
     * <p>
     * Retrieves and displays the details of an address.
     * </p>
     *
     * @param id the ID of the address whose details are to be viewed
     * @return the Address object.
     * @throws NoSuchElementException when occurred.
     */
    public AddressDto getAddressById(String id) {
        try {
            Address address = addressRepository.findByIdAndIsDeletedFalse(id);
            if (address == null) {
                throw new NoSuchElementException("Address not found for the given id: " + id);
            }
            logger.info("Retrieved address details for ID: {}", id);
            return AddressMapper.modelToDto(address);
        } catch (Exception e) {
            if (e instanceof NoSuchElementException) {
                logger.error("Address not found for the given ID: {} ", id, e);
                throw e;
            }
            logger.error("Error in retrieving an address for the id : {}", id, e);
            throw new CustomException("Server Error!!!!", e);
        }
    }

    public void deleteAddressById(String id) {
        try {
            Address address = addressRepository.findByIdAndIsDeletedFalse(id);
            if (address == null) {
                throw new NoSuchElementException("Address not found for the given id: " + id);
            }
            address.setIsDeleted(true);
            addressRepository.save(address);
            logger.info("Address removed successfully with ID: {}", id);
        } catch (Exception e) {
            if (e instanceof NoSuchElementException) {
                logger.error("Address not found for the id: {}", id, e);
                throw e;
            }
            logger.error("Error in retrieving an address : {}", id, e);
            throw new CustomException("Server Error!!!!", e);
        }
    }

    public Address getAddressModelById(String id) {
        try {
            Address address = addressRepository.findByIdAndIsDeletedFalse(id);
            logger.info("Retrieved address for the given id: {}", id);
            return address;
        } catch (Exception e) {
            logger.error("Error in retrieving address for the given id: {}", id);
            throw new CustomException("Server error!!", e);
        }
    }
}







