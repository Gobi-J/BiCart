package com.bicart.service;

import com.bicart.dto.ReviewDto;
import com.bicart.helper.CustomException;
import com.bicart.mapper.ReviewMapper;
import com.bicart.model.Product;
import com.bicart.model.Review;
import com.bicart.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * <p>
 * Service class that handles business logic related to reviews.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    private final ProductService productService;


    private static final Logger logger = LogManager.getLogger(ReviewService.class);
    private final UserService userService;

    /**
     * <p>
     * Saves a review.
     * </p>
     *
     * @param review model object to be saved.
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
     * @return {@link ReviewDto} created reviewDto object.
     * @throws CustomException, DuplicateKeyException if exception is thrown.
     */
    public ReviewDto addReview(String userId, ReviewDto reviewDTO, String productId) {
        try {
            Review review = ReviewMapper.dtoToModel((reviewDTO));
            Product product = productService.getProductById(productId);
            review.setId(UUID.randomUUID().toString());
            review.setProduct(product);
            review.setIsDeleted(false);
            review.setCreatedAt(new Date());
            review.setCreatedBy(userId);
            review.setUser(userService.getUserModelById(userId));
            saveReview(review);
            ReviewDto reviewDto = ReviewMapper.modelToDto((review));
            logger.info("Review added successfully with ID: {}", reviewDto.getId());
            return reviewDto;
        } catch (Exception e) {
            logger.error("Error adding a review with Product ID: {}", productId, e);
            throw new CustomException("Server Error!!!!", e);
        }
    }

    /**
     * <p>
     * Fetch all reviews given by the user
     * </p>
     *
     * @param userId whose reviews are to be fetched
     * @param page   entries from which page are to be fetched
     * @param size   number of entries needed
     * @return {@link Set<ReviewDto>} set of all reviews given by the user
     * @throws CustomException if any exception is thrown
     */
    public Set<ReviewDto> getAllReviewsByUserId(String userId, int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Review> reviews = reviewRepository.findByUserIdAndIsDeletedFalse(userId, pageable);
            logger.info("Displayed user's reviews for page : {}", page);
            return reviews.getContent().stream()
                    .map(ReviewMapper::modelToDto)
                    .collect(Collectors.toSet());
        } catch (Exception e) {
            logger.error("Error getting all reviews by user with id {}", userId, e);
            throw new CustomException("Server error!!", e);
        }
    }

    /**
     * <p>
     * Fetch all reviews of the given product
     * </p>
     *
     * @param productId whose reviews are to be fetched
     * @param page   entries from which page are to be fetched
     * @param size   number of entries needed
     * @return {@link Set<ReviewDto>} set of all reviews of the given product
     * @throws CustomException if any exception is thrown
     */
    public Set<ReviewDto> getReviewsByProductId(String productId, int page, int size) throws CustomException {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Review> reviews = reviewRepository.findByProductIdAndIsDeletedFalse(productId, pageable);
            logger.info("Displayed product's reviews for page : {}", page);
            return reviews.getContent().stream()
                    .map(ReviewMapper::modelToDto)
                    .collect(Collectors.toSet());
        } catch (Exception e) {
            logger.error("Error getting all reviews of product with id {}", productId, e);
            throw new CustomException("Server error!!", e);
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
    public ReviewDto getReviewById(String id) {
        try {
            Review review = reviewRepository.findByIdAndIsDeletedFalse(id);
            if (review == null) {
                throw new NoSuchElementException("Review not found for the given id: " + id);
            }
            logger.info("Retrieved review details for ID: {}", id);
            return ReviewMapper.modelToDto(review);
        } catch (Exception e) {
            if (e instanceof NoSuchElementException) {
                logger.error("Review not found for ID: {}", id, e);
                throw e;
            }
            logger.error("Error in retrieving a review : {}", id, e);
            throw new CustomException("Server Error!!!!", e);
        }
    }
}







