package com.bicart.controller;

import java.util.Map;
import java.util.Set;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bicart.dto.AddressDto;
import com.bicart.helper.SuccessResponse;
import com.bicart.service.AddressService;

/**
 * <p>
 * AddressController class is a REST controller class that handles all the address related requests.
 * </p>
 */
@RestController
@RequestMapping("v1/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    /**
     * <p>
     * Adds a new address to the database.
     * </p>
     *
     * @param addressDto The address details to be added.
     * @return {@link SuccessResponse} with {@link HttpStatus} CREATED
     */
    @PostMapping
    public ResponseEntity<SuccessResponse> addAddress(@Validated @RequestAttribute("id") String userId, @RequestBody AddressDto addressDto) {
        addressService.addAddress(addressDto, userId);
        return SuccessResponse.setSuccessResponseCreated("Address Added Successful", null);
    }

    /**
     * <p>
     * Updates an existing address in the database.
     * </p>
     *
     * @param addressDto The address details to be updated.
     * @return {@link SuccessResponse} with updated address details and {@link HttpStatus} OK
     */
    @PatchMapping
    public ResponseEntity<SuccessResponse> updateAddress(@RequestBody AddressDto addressDto) {
        AddressDto address = addressService.updateAddress(addressDto);
        return SuccessResponse.setSuccessResponseOk("User updated successfully", address);
    }

    /**
     * <p>
     * Gets all the addresses from the database.
     * </p>
     *
     * @param page The page number to be fetched.
     * @param size The number of addresses to be fetched.
     * @return {@link SuccessResponse} with all the addresses and {@link HttpStatus} OK
     */
    @GetMapping
    public ResponseEntity<SuccessResponse> getAllAddresses(@RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "10") int size,
                                                           @RequestAttribute("id") String userId) {
        Set<AddressDto> addresses = addressService.getAllAddresses(userId, page, size);
        return SuccessResponse.setSuccessResponseOk("Addresses fetched successfully", Map.of("addresses", addresses));
    }

    /**
     * <p>
     * Gets an address by its id.
     * </p>
     *
     * @param addressId The id of the address to be fetched.
     * @return {@link SuccessResponse} with address details and {@link HttpStatus} OK
     */
    @GetMapping("/{addressId}")
    public ResponseEntity<SuccessResponse> getAddressById(@PathVariable String addressId) {
        AddressDto address = addressService.getAddressById(addressId);
        return SuccessResponse.setSuccessResponseOk("Address fetched successfully", Map.of("address", address));
    }

    /**
     * <p>
     * Deletes an address by its id.
     * </p>
     *
     * @param addressId The id of the address to be deleted.
     * @return {@link SuccessResponse} with {@link HttpStatus} NO_CONTENT
     */
    @DeleteMapping("/{addressId}")
    public ResponseEntity<SuccessResponse> removeAddress(@PathVariable String addressId) {
        addressService.deleteAddressById(addressId);
        return SuccessResponse.setSuccessResponseNoContent("Address deleted successfully");
    }
}
