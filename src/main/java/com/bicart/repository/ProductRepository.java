package com.bicart.repository;

import com.bicart.model.Product;
import jakarta.validation.constraints.NotBlank;
import lombok.NonNull;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface ProductRepository extends JpaRepository<Product, String> {
    Product findByIdAndIsDeletedFalse(@NonNull String id);

    boolean existsByName(@NotBlank(message = "Product name should not be blank") String name);

    Set<Product> findAllByIsDeletedFalse(Pageable pageable);

    Set<Product> findAllBySubCategoryNameAndIsDeletedFalse(@NonNull String subCategoryName, Pageable pageable);
}
