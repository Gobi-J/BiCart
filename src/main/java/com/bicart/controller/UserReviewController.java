package com.bicart.controller;

import com.bicart.dto.ReviewDto;
import com.bicart.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("v1/users/{userId}/reviews")
public class UserReviewController {

    @Autowired
    private ReviewService reviewService;

    @GetMapping("/{userId}/reviews")
    public ResponseEntity<Set<ReviewDto>> getAllReviewsByUserId(@RequestParam(defaultValue = "0") int page,
                                                                @RequestParam(defaultValue = "10") int size,
                                                                @PathVariable String userId) {
        return new ResponseEntity<>(reviewService.getAllReviewsByUserId(userId, page, size), HttpStatus.OK);
    }
}
