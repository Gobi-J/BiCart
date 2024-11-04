package com.bicart.repository;

import com.bicart.dto.ReviewDto;
import com.bicart.model.Review;
import com.bicart.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * Repository interface for performing CRUD operations on the Review entity.
 */
@Repository
public interface ReviewRepository extends JpaRepository<Review, String> {
    Page<Review> findAllByIsDeletedFalse(Pageable pageable);

    Review findByIdAndIsDeletedFalse(String id);

    Page<Review> findByUserIdAndIsDeletedFalse(String userId, Pageable pageable);

    Page<Review> findByProductIdAndIsDeletedFalse(String productId, Pageable pageable);
}
