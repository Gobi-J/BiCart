package com.bicart.controller;

import com.bicart.dto.ReviewDto;
import com.bicart.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *     ReviewController class is a REST controller class that handles all the review related requests.
 * </p>
 */
@RestController
@RequestMapping("v1/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    /**
     * <p>
     *     Adds a review for a product.
     * </p>
     *
     * @param productId to which the review is added
     * @param reviewDto review details to be added
     * @return {@link ResponseEntity<ReviewDto>} review details that are added with {@link HttpStatus} CREATED
     */
    @PostMapping("/{productId}/reviews")
    public ResponseEntity<ReviewDto> addReview(@PathVariable String productId, @RequestBody ReviewDto reviewDto) {
        return new ResponseEntity<>(reviewService.addReview(reviewDto, productId), HttpStatus.CREATED);
    }

    /**
     * <p>
     *     Gets all the reviews for a product.
     * </p>
     *
     * @param reviewId for which reviews are fetched
     * @return {@link ResponseEntity<ReviewDto>} reviews for the product with {@link HttpStatus} OK
     */
    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewDto> getReviewById(@PathVariable String reviewId) {
        return new ResponseEntity<>(reviewService.getReviewById(reviewId), HttpStatus.OK);
    }
}
