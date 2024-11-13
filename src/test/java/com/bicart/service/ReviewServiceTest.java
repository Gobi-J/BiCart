package com.bicart.service;

import java.util.List;
import java.util.Set;

import com.bicart.dto.ReviewDto;
import com.bicart.model.Product;
import com.bicart.model.Review;
import com.bicart.model.User;
import com.bicart.repository.ReviewRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    private Review review;
    private ReviewDto reviewDto;
    private User user;
    private Product product;

    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private ProductService productService;
    @Mock
    private UserService userService;

    @InjectMocks
    private ReviewService reviewService;

    @BeforeEach
    void setUp() {
        review = Review.builder()
                .message("Review Message")
                .rating(5)
                .build();

        reviewDto = ReviewDto.builder()
                .message("Review Message")
                .rating(5)
                .build();

        user = User.builder()
                .id("1")
                .build();

        product = Product.builder()
                .id("1")
                .build();
    }

    @Test
    void testAddReviewSuccess() {
        when(productService.getProductModelById(anyString())).thenReturn(product);
        when(userService.getUserModelById(anyString())).thenReturn(user);
        when(reviewRepository.save(any(Review.class))).thenReturn(review);
        reviewService.addReview("1", reviewDto, "1");
        verify(reviewRepository, times(1)).save(any(Review.class));
    }

    @Test
    void testGetReviewsByProductIdSuccess() {
        when(reviewRepository.findByProductIdAndIsDeletedFalse(anyString(), any())).thenReturn(List.of(review));
        Set<ReviewDto> result = reviewService.getReviewsByProductId("1",0, 1);
        assertEquals(1, result.size());
    }
}