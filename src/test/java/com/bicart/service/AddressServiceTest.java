package com.bicart.service;

import com.bicart.dto.AddressDto;
import com.bicart.helper.CustomException;
import com.bicart.model.Address;
import com.bicart.model.User;
import com.bicart.repository.AddressRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddressServiceTest {

    @InjectMocks
    private AddressService addressService;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private UserService userService;

    private Address address;
    private AddressDto addressDto;
    private User user;

    @BeforeEach
    void setUp() {
        address = Address.builder()
                .id("1")
                .city("testCity")
                .country("testCountry")
                .build();

        addressDto = AddressDto.builder()
                .id("1")
                .city("testCity")
                .country("testCountry")
                .build();

        user = User.builder()
                .id("1")
                .name("TestName")
                .mobileNumber("9234567890")
                .build();

    }

    @Test
    void testSaveAddressSuccess() {
        when(addressRepository.save(any(Address.class))).thenReturn(address);
        addressService.saveAddress(address);
        verify(addressRepository, times(1)).save(any(Address.class));
    }

    @Test
    void testSaveAddressThrowsException() {
        when(addressRepository.save(any(Address.class))).thenThrow(CustomException.class);
        assertThrows(CustomException.class, () -> addressService.saveAddress(address));
    }

    @Test
    void testAddAddressSuccess() {
        when(addressRepository.save(any(Address.class))).thenReturn(address);
        when(userService.getUserModelById("1")).thenReturn(user);
        addressService.addAddress(addressDto, "1");
        verify(addressRepository, times(1)).save(any(Address.class));
    }


    @Test
    void testGetAllAddressesSuccess() {
        when(addressRepository.findAllByUserIdAndIsDeletedFalse("1", PageRequest.of(0, 1))).thenReturn(List.of(address));
        assertEquals(1, addressService.getAllAddresses("1", 0, 1).size());
    }

    @Test
    void testUpdateAddressSuccess() {
        when(addressRepository.save(any(Address.class))).thenReturn(address);
        AddressDto result = addressService.updateAddress(addressDto);
        assertNotNull(result);
        assertEquals(addressDto.getCity(), result.getCity());
    }

    @Test
    void testGetAddressByIdSuccess() {
        when(addressRepository.findByIdAndIsDeletedFalse("1")).thenReturn(address);
        AddressDto result = addressService.getAddressById("1");
        assertNotNull(result);
        assertEquals(addressDto.getCity(), result.getCity());
    }

    @Test
    void testDeleteAddressByIdSuccess() {
        when(addressRepository.findByIdAndIsDeletedFalse("1")).thenReturn(address);
        when(addressRepository.save(any(Address.class))).thenReturn(address);
        addressService.deleteAddressById("1");
    }

    @Test
    void testGetAddressModelByUserIdSuccess() {
        when(addressRepository.findByIdAndIsDeletedFalse(anyString())).thenReturn(address);
        Address result = addressService.getAddressModelById("1");
        assertNotNull(result);
        assertEquals(result.getCity(), address.getCity());
    }

    @Test
    void testGetAddressModelByUserIdThrowsNoSuchElementException() {
        when(addressRepository.findByUserIdAndIsDeletedFalse(anyString())).thenReturn(null);
        assertThrows(NoSuchElementException.class,() -> addressService.getAddressModelByUserId("1"));
    }

}