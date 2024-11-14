package com.bicart.repository;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.bicart.model.SubCategory;

/**
 * Repository interface for performing CRUD operations on the SubCategory entity.
 */
public interface SubCategoryRepository extends JpaRepository<SubCategory, String> {
    SubCategory findByNameAndIsDeletedFalse(String name);

    boolean existsByName(@NotBlank(message = "Sub category must have a name") String name);

    List<SubCategory> findAllByIsDeletedFalse(Pageable pageable);
}
