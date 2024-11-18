package com.bicart.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.bicart.dto.AddressDto;
import com.bicart.helper.BiCartException;
import com.bicart.mapper.AddressMapper;
import com.bicart.model.Address;
import com.bicart.repository.AddressRepository;

/**
 * <p>
 * Service class that handles business logic related to address.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;
    private final UserService userService;

    private static final Logger logger = LogManager.getLogger(AddressService.class);

    /**
     * <p>
     * Saves an Address.
     * </p>
     *
     * @param address details to save.
     * @throws BiCartException if any issues occur while saving.
     */
    protected void saveAddress(Address address) {
        try {
            addressRepository.save(address);
            logger.info("Address saved successfully with the id : {} ", address.getId());
        } catch (Exception e) {
            logger.error("Error in saving address with the id : {}", address.getId());
            throw new BiCartException("Cannot save address with the id : " + address.getId());
        }
    }

    /**
     * <p>
     * Creates a new Address object and saves it in the repository.
     * </p>
     *
     * @param addressDTO to create a new address.
     */
    public void addAddress(AddressDto addressDTO, String userId) {
        Address address = AddressMapper.dtoToModel(addressDTO);
        address.setUser(userService.getUserModelById(userId));
        address.setAudit(userId);
        saveAddress(address);
        logger.info("Address added successfully with Id {}", address.getId());
    }

    /**
     * <p>
     * Retrieves and displays all address of a given user
     * </p>
     *
     * @param userId the ID of the user whose address details are to be viewed
     * @param page entries from which page are to be fetched
     * @param size number of entries needed
     * @return {@link AddressDto} set containing details of all the Address.
     */
    public Set<AddressDto> getAllAddresses(String userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<Address> addresses = addressRepository.findAllByUserIdAndIsDeletedFalse(userId, pageable);
        logger.info("Displayed address details for user : {}", userId);
        return addresses.stream()
                .map(AddressMapper::modelToDto)
                .collect(Collectors.toSet());
    }

    /**
     * <p>
     * Updates the address with new details.
     * </p>
     *
     * @param addressDto to update all the details of the address.
     * @return {@link AddressDto} which is updated.
     */
    public AddressDto updateAddress(AddressDto addressDto) {
        Address address = AddressMapper.dtoToModel(addressDto);
        saveAddress(address);
        logger.info("Address updated successfully for ID: {}", addressDto.getId());
        return AddressMapper.modelToDto(address);
    }

    /**
     * <p>
     * Retrieves and displays the details of an address.
     * </p>
     *
     * @param id of the address whose details are to be viewed
     * @return {@link AddressDto} which to be fetched.
     */
    public AddressDto getAddressById(String id) {
        Address address = getAddressModelById(id);
        logger.info("Retrieved address details for ID: {}", id);
        return AddressMapper.modelToDto(address);
    }

    /**
     * <p>
     * Deletes the address with the given ID.
     * </p>
     *
     * @param id the ID of the address to be deleted.
     * @throws NoSuchElementException when occurred.
     */
    public void deleteAddressById(String id) {
        Address address = getAddressModelById(id);
        address.setIsDeleted(true);
        saveAddress(address);
        logger.info("Address removed successfully with ID: {}", id);
    }

    /**
     * <p>
     * Retrieves and displays the details of an address.
     * </p>
     *
     * @param id of the user whose address details are to be viewed
     * @return {@link Address} which to be fetched.
     * @throws NoSuchElementException if address not found.
     */
    protected Address getAddressModelById(String id) {
        Address address = addressRepository.findByIdAndIsDeletedFalse(id);
        if (address == null) {
            logger.error("Address not found for the given id: {}", id);
            throw new NoSuchElementException("Address not found for the given id: " + id);
        }
        logger.info("Retrieved address for the given id: {}", id);
        return address;
    }

    /**
     * <p>
     * Retrieves and displays the details of an address.
     * </p>
     *
     * @param id of the user whose address details are to be viewed
     * @return {@link Address} which to be fetched.
     * @throws NoSuchElementException if address not found.
     */
    protected Address getAddressModelByUserId(String id) {
        List<Address> address = addressRepository.findByUserIdAndIsDeletedFalse(id);
        if (address == null || address.isEmpty()) {
            logger.error("Address not found for the given user: {}", id);
            throw new NoSuchElementException("Address not found for the given user: " + id);
        }
        logger.info("Retrieved address for the given user: {}", id);
        return address.getFirst();
    }

    /**
     * <p>
     * Deletes the address with the given user ID.
     * </p>
     *
     * @param userId of the user whose address to be deleted.
     */
    protected void deleteAddressWithUserId(String userId) {
        List<Address> addresses = addressRepository.findByUserIdAndIsDeletedFalse(userId);
        addresses.forEach(address -> {
            address.setIsDeleted(true);
            saveAddress(address);
        });
    }
}







