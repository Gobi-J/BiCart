package com.bicart.dto;

import com.bicart.model.Product;
import com.bicart.model.User;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ReviewDto {

    private String message;
    private int rating;
    private UserDto user;
    private ProductDto product;
}
