package com.bicart.repository;

import com.bicart.model.Category;
import lombok.NonNull;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, String> {
    Category findByNameAndIsDeletedFalse(String name);

    boolean existsByName(String name);

    List<Category> findAllByIsDeletedFalse(Pageable pageable);

    Category findByName(String unknown);
}
