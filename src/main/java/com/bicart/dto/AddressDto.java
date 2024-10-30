package com.bicart.dto;

import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AddressDto {

    private String id;

    @NotBlank(message = "Door number is mandatory")
    private String doorNumber;

    @NotBlank(message = "Street is mandatory")
    private String street;

    @NotBlank(message = "City is mandatory")
    private String city;

    @NotBlank(message = "State is mandatory")
    private String state;

    @NotBlank(message = "Country is mandatory")
    private String country;

    @NotBlank(message = "Pin code is mandatory")
    @Pattern(regexp = "^[0-9]+")
    private String pinCode;

    @NotBlank(message = "mobileNumber is mandatory")
    @Pattern(regexp = "^[1-9][0-9]{9}$", message = " Phone number should contain only numbers")
    private String phone;

    private UserDto user;
}
