package com.bicart.mapper;

import com.bicart.dto.PaymentDto;
import com.bicart.dto.ReviewDto;
import com.bicart.model.Payment;
import com.bicart.model.Review;

public class ReviewMapper {
    public static ReviewDto modelToDto(Review review) {
        return ReviewDto.builder()
                .id(review.getId())
                .message(review.getMessage())
                .rating(review.getRating())
                .user((review.getUser() != null) ? UserMapper.modelToDto(review.getUser()) : null)
                .product((review.getProduct() != null) ? ProductMapper.modelToDto(review.getProduct()) : null)
                .build();
    }

    public static Review dtoToModel(ReviewDto reviewDto) {
        return Review.builder()
                .id(reviewDto.getId())
                .message(reviewDto.getMessage())
                .rating(reviewDto.getRating())
                .user((reviewDto.getUser() != null) ? UserMapper.dtoToModel(reviewDto.getUser()) : null)
                .product((reviewDto.getProduct() != null) ? ProductMapper.dtoToModel(reviewDto.getProduct()) : null)
                .build();
    }
}
