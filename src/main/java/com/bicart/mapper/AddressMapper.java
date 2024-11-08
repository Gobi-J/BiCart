package com.bicart.mapper;

import com.bicart.dto.AddressDto;
import com.bicart.model.Address;

public class AddressMapper {

    public static AddressDto modelToDto(Address address) {
        return AddressDto.builder()
                .doorNumber(address.getDoorNumber())
                .street(address.getStreet())
                .city(address.getCity())
                .state(address.getState())
                .country(address.getCountry())
                .pinCode(address.getPinCode())
                .phone(address.getPhone())
                .build();
    }

    public static Address dtoToModel(AddressDto addressDto) {
        return Address.builder()
                .id(addressDto.getId())
                .doorNumber(addressDto.getDoorNumber())
                .street(addressDto.getStreet())
                .city(addressDto.getCity())
                .state(addressDto.getState())
                .country(addressDto.getCountry())
                .pinCode(addressDto.getPinCode())
                .phone(addressDto.getPhone())
                .user((addressDto.getUser() != null) ? UserMapper.dtoToModel(addressDto.getUser()) : null)
                .build();
    }
}
