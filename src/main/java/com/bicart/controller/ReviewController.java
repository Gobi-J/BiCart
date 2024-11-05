package com.bicart.controller;

import com.bicart.dto.ReviewDto;
import com.bicart.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("v1/reviews")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;

    @PostMapping("/{productId}/reviews")
    public ResponseEntity<ReviewDto> addReview(@PathVariable String productId, @RequestBody ReviewDto reviewDto) {
        return new ResponseEntity<>(reviewService.addReview(reviewDto, productId), HttpStatus.CREATED);
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewDto> getReviewById(@PathVariable String reviewId){
        return new ResponseEntity<>(reviewService.getReviewById(reviewId), HttpStatus.OK);
    }
}
