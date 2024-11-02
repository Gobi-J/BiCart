package com.bicart.repository;

import com.bicart.model.SubCategory;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubCategoryRepository extends JpaRepository<SubCategory, String> {
    SubCategory findByNameAndIsDeletedFalse(String name);

    boolean existsByName(@NotBlank(message = "Sub category must have a name") String name);
}
