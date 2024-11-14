package com.bicart.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
