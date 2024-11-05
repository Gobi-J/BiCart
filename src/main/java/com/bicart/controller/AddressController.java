package com.bicart.controller;

import com.bicart.dto.AddressDto;
import com.bicart.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("v1/addresses")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @PostMapping
    public ResponseEntity<AddressDto> addAddress(@RequestBody AddressDto addressDto) {
        return new ResponseEntity<>(addressService.addAddress(addressDto), HttpStatus.CREATED);
    }

    @PatchMapping
    public ResponseEntity<AddressDto> updateAddress(@RequestBody AddressDto addressDto) {
        return new ResponseEntity<>(addressService.updateAddress(addressDto), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Set<AddressDto>> getAllAddresses(@RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "10") int size) {
        return new ResponseEntity<>(addressService.getAllAddresses(page, size), HttpStatus.OK);
    }

    @GetMapping("/{addressId}")
    public ResponseEntity<AddressDto> getAddressById(@PathVariable String addressId){
        return new ResponseEntity<>(addressService.getAddressById(addressId), HttpStatus.OK);
    }

    @DeleteMapping("/{addressId}")
    public ResponseEntity<String> deleteAddress(@PathVariable String addressId) {
        addressService.deleteAddressById(addressId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
