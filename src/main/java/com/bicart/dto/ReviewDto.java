package com.bicart.dto;

import com.bicart.model.Product;
import com.bicart.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ReviewDto {

    private String message;
    private int rating;
    @JsonIgnore
    private UserDto user;
    @JsonIgnore
    private ProductDto product;
}
