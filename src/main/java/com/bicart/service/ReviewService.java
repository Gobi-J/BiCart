package com.bicart.service;

import com.bicart.dto.ReviewDto;
import com.bicart.helper.CustomException;
import com.bicart.mapper.ReviewMapper;
import com.bicart.model.Review;
import com.bicart.repository.ReviewRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    private static final Logger logger = LogManager.getLogger(ReviewService.class);
    /**
     * <p>
     * Saves a review.
     * </p>
     *
     * @param review model.
     */
    public void saveReview(Review review) {
        try {
            reviewRepository.save(review);
            logger.info("Review saved successfully");
        } catch (Exception e) {
            logger.error("Error in saving review");
            throw new CustomException("Server error!!", e);
        }
    }

    /**
     * <p>
     * Creates a new Review object and saves it in the repository.
     * </p>
     *
     * @param reviewDTO to create new review.
     * @return the created reviewDto object.
     * @throws CustomException, DuplicateKeyException if exception is thrown.
     */
    public ReviewDto addReview(ReviewDto reviewDTO) throws CustomException {
        try {
            Review review = ReviewMapper.dtoToModel((reviewDTO));
            reviewRepository.save(review);
            ReviewDto reviewDto = ReviewMapper.modelToDto((review));
            logger.info("Review added successfully with ID: {}", reviewDto.getId());
            return reviewDto;
        } catch (Exception e) {
            logger.error("Error adding a review", e);
            throw new CustomException("Server Error!!!!", e);
        }
    }

    /**
     * <p>
     * Retrieves and displays all reviews.
     * </p>
     *
     * @return {@link Set <ReviewDto>} all the Reviews.
     * @throws CustomException, when any custom Exception is thrown.
     */
    public Set<ReviewDto> getAllReviews(int page, int size) throws CustomException {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Review> reviewPage = reviewRepository.findAllByIsDeletedFalse(pageable);
            logger.info("Displayed review details for page : {}", page);
            return reviewPage.getContent().stream()
                    .map(ReviewMapper::modelToDto)
                    .collect(Collectors.toSet());
        } catch (Exception e) {
            logger.error("Error in retrieving all reviews", e);
            throw new CustomException("Server Error!!!!", e);
        }
    }

    /**
     * <p>
     * Retrieves and displays the details of an review.
     * </p>
     *
     * @param id the ID of the review whose details are to be viewed
     * @return the Review object.
     * @throws NoSuchElementException when occurred.
     */
    public ReviewDto getReviewById(String id) throws NoSuchElementException, CustomException {
        try {
            Review review = reviewRepository.findByIdAndIsDeletedFalse(id);
            if (review == null) {
                throw new NoSuchElementException("Review not found for the given id: " + id);
            }
            logger.info("Retrieved review details for ID: {}", id);
            return ReviewMapper.modelToDto(review);
        } catch (NoSuchElementException e) {
            logger.error("Review not found", e);
            throw e;
        } catch (Exception e) {
            logger.error("Error in retrieving an review : {}", id, e);
            throw new CustomException("Server error!!", e);
        }
    }
}







