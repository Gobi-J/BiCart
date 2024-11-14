package com.bicart.repository;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import lombok.NonNull;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.bicart.model.Product;

/**
 * Repository interface for performing CRUD operations on the Product entity.
 */
public interface ProductRepository extends JpaRepository<Product, String> {
    Product findByIdAndIsDeletedFalse(@NonNull String id);

    boolean existsByName(@NotBlank(message = "Product name should not be blank") String name);

    List<Product> findAllByIsDeletedFalse(Pageable pageable);

    List<Product> findAllBySubCategoryNameAndIsDeletedFalse(@NonNull String subCategoryName, Pageable pageable);
}
