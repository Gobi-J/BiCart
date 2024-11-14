package com.bicart.mapper;

import com.bicart.dto.ReviewDto;
import com.bicart.model.Review;

public class ReviewMapper {
    public static ReviewDto modelToDto(Review review) {
        return ReviewDto.builder()
                .message(review.getMessage())
                .rating(review.getRating())
                .build();
    }

    public static Review dtoToModel(ReviewDto reviewDto) {
        return Review.builder()
                .message(reviewDto.getMessage())
                .rating(reviewDto.getRating())
                .user((reviewDto.getUser() != null) ? UserMapper.dtoToModel(reviewDto.getUser()) : null)
                .product((reviewDto.getProduct() != null) ? ProductMapper.dtoToModel(reviewDto.getProduct()) : null)
                .build();
    }
}
