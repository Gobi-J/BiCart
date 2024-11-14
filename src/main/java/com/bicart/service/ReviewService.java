package com.bicart.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.bicart.dto.ReviewDto;
import com.bicart.helper.CustomException;
import com.bicart.mapper.ReviewMapper;
import com.bicart.model.Product;
import com.bicart.model.Review;
import com.bicart.repository.ReviewRepository;

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
    private final UserService userService;

    private static final Logger logger = LogManager.getLogger(ReviewService.class);

    /**
     * <p>
     * Saves a review.
     * </p>
     *
     * @param review model object to be saved.
     * @throws CustomException if any issue occurs while saving the review.
     */
    private void saveReview(Review review) {
        try {
            reviewRepository.save(review);
            logger.info("Review saved successfully");
        } catch (Exception e) {
            logger.error("Error in saving review");
            throw new CustomException("Cannot save review for product " + review.getProduct().getName(), e);
        }
    }

    /**
     * <p>
     * Creates a new Review object and saves it in the repository.
     * </p>
     *
     * @param userId    who is adding the review.
     * @param reviewDTO to create a new review.
     * @param productId in which review is to be added
     */
    public void addReview(String userId, ReviewDto reviewDTO, String productId) {
        Review review = ReviewMapper.dtoToModel(reviewDTO);
        Product product = productService.getProductModelById(productId);
        review.setProduct(product);
        review.setAudit(userId);
        review.setUser(userService.getUserModelById(userId));
        saveReview(review);
        logger.info("Review added successfully with ID: {}", review.getId());
    }

    /**
     * <p>
     * Fetch all reviews of the given product
     * </p>
     *
     * @param productId whose reviews are to be fetched
     * @param page   entries from which page are to be fetched
     * @param size   number of entries needed
     * @return {@link ReviewDto} set containing all reviews of the given product
     */
    public Set<ReviewDto> getReviewsByProductId(String productId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<Review> reviews = reviewRepository.findByProductIdAndIsDeletedFalse(productId, pageable);
        logger.info("Displayed product's reviews for page : {}", page);
        return reviews.stream()
                .map(ReviewMapper::modelToDto)
                .collect(Collectors.toSet());
    }
}