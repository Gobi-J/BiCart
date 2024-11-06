package com.bicart.controller;

import com.bicart.dto.AddressDto;
import com.bicart.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * <p>
 *     AddressController class is a REST controller class that handles all the address related requests.
 * </p>
 */
@RestController
@RequestMapping("v1/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    /**
     * <p>
     *     Adds a new address to the database.
     * </p>
     * @param addressDto The address details to be added.
     * @return {@link ResponseEntity<AddressDto>} address details that were added with {@link HttpStatus} CREATED
     */
    @PostMapping
    public ResponseEntity<AddressDto> addAddress(@RequestAttribute("id") String userId, @RequestBody AddressDto addressDto) {
        return new ResponseEntity<>(addressService.addAddress(addressDto, userId), HttpStatus.CREATED);
    }

    /**
     * <p>
     *     Updates an existing address in the database.
     * </p>
     * @param addressDto The address details to be updated.
     * @return {@link ResponseEntity<AddressDto>} address details that were updated with {@link HttpStatus} OK
     */
    @PatchMapping
    public ResponseEntity<AddressDto> updateAddress(@RequestBody AddressDto addressDto) {
        return new ResponseEntity<>(addressService.updateAddress(addressDto), HttpStatus.OK);
    }

    /**
     * <p>
     *     Gets all the addresses from the database.
     * </p>
     * @param page The page number to be fetched.
     * @param size The number of addresses to be fetched.
     * @return {@link ResponseEntity<Set<AddressDto>} addresses with {@link HttpStatus} OK
     */
    @GetMapping
    public ResponseEntity<Set<AddressDto>> getAllAddresses(@RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "10") int size) {
        return new ResponseEntity<>(addressService.getAllAddresses(page, size), HttpStatus.OK);
    }

    /**
     * <p>
     *     Gets an address by its id.
     * </p>
     * @param addressId The id of the address to be fetched.
     * @return {@link ResponseEntity<AddressDto>} address details with {@link HttpStatus} OK
     */
    @GetMapping("/{addressId}")
    public ResponseEntity<AddressDto> getAddressById(@PathVariable String addressId){
        return new ResponseEntity<>(addressService.getAddressById(addressId), HttpStatus.OK);
    }

    /**
     * <p>
     *     Deletes an address by its id.
     * </p>
     * @param addressId The id of the address to be deleted.
     * @return {@link ResponseEntity<String>} with {@link HttpStatus} NO_CONTENT
     */
    @DeleteMapping("/{addressId}")
    public ResponseEntity<String> deleteAddress(@PathVariable String addressId) {
        addressService.deleteAddressById(addressId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
