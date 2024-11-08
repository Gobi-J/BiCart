package com.bicart.controller;

import java.util.Set;

import com.bicart.helper.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.bicart.dto.AddressDto;
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
     * @return {@link ResponseEntity<AddressDto>} address details that were added with {@link HttpStatus} CREATED
     */
    @PostMapping
    public ResponseEntity<SuccessResponse> addAddress(@Validated @RequestAttribute("id") String userId, @RequestBody AddressDto addressDto) {
        addressService.addAddress(addressDto, userId);
        return SuccessResponse.setSuccessResponse("Address Added Successful", HttpStatus.CREATED);
    }

    /**
     * <p>
     * Updates an existing address in the database.
     * </p>
     *
     * @param addressDto The address details to be updated.
     * @return {@link ResponseEntity<AddressDto>} address details that were updated with {@link HttpStatus} OK
     */
    @PatchMapping
    public ResponseEntity<SuccessResponse> updateAddress(@RequestBody AddressDto addressDto) {
        AddressDto address = addressService.updateAddress(addressDto);
        return SuccessResponse.setSuccessResponse("User updated successfully", HttpStatus.OK, address);
    }

    /**
     * <p>
     * Gets all the addresses from the database.
     * </p>
     *
     * @param page The page number to be fetched.
     * @param size The number of addresses to be fetched.
     * @return {@link ResponseEntity<Set<AddressDto>} addresses with {@link HttpStatus} OK
     */
    @GetMapping
    public ResponseEntity<SuccessResponse> getAllAddresses(@RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "10") int size) {
        Set<AddressDto> addresses = addressService.getAllAddresses(page, size);
        return SuccessResponse.setSuccessResponse("Addresses fetched successfully", HttpStatus.OK, addresses);
    }

    /**
     * <p>
     * Gets an address by its id.
     * </p>
     *
     * @param addressId The id of the address to be fetched.
     * @return {@link ResponseEntity<AddressDto>} address details with {@link HttpStatus} OK
     */
    @GetMapping("/{addressId}")
    public ResponseEntity<SuccessResponse> getAddressById(@PathVariable String addressId) {
        AddressDto addressDto = addressService.getAddressById(addressId);
        return SuccessResponse.setSuccessResponse("Address fetched successfully", HttpStatus.OK, addressDto);
    }

    /**
     * <p>
     * Deletes an address by its id.
     * </p>
     *
     * @param addressId The id of the address to be deleted.
     * @return {@link ResponseEntity<String>} with {@link HttpStatus} NO_CONTENT
     */
    @DeleteMapping("/{addressId}")
    public ResponseEntity<SuccessResponse> removeAddress(@PathVariable String addressId) {
        addressService.deleteAddressById(addressId);
        return SuccessResponse.setSuccessResponse("Address deleted successfully", HttpStatus.NO_CONTENT);
    }
}
