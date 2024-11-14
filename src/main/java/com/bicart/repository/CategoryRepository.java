package com.bicart.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.bicart.model.Category;

/**
 * Repository interface for performing CRUD operations on the Category entity.
 */
public interface CategoryRepository extends JpaRepository<Category, String> {
    Category findByNameAndIsDeletedFalse(String name);

    boolean existsByName(String name);

    List<Category> findAllByIsDeletedFalse(Pageable pageable);
}
