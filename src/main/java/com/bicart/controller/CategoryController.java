package com.bicart.controller;

import java.util.Map;
import java.util.Set;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bicart.dto.CategoryDto;
import com.bicart.helper.SuccessResponse;
import com.bicart.service.CategoryService;

/**
 * <p>
 * CategoryController class is a REST controller class that handles all the category related requests.
 * </p>
 */
@RestController
@RequestMapping("/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * <p>
     * Add a new category
     * </p>
     *
     * @param category Category to be added
     * @return {@link SuccessResponse} with {@link HttpStatus} CREATED
     */
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<SuccessResponse> addCategory(@Validated @RequestBody CategoryDto category) {
        categoryService.addCategory(category);
        return SuccessResponse.setSuccessResponseCreated("Category Added Successfully", null);
    }

    /**
     * <p>
     * Get the category by name
     * </p>
     *
     * @param categoryName Name of the category
     * @return {@link SuccessResponse} with the category and {@link HttpStatus} OK
     */
    @GetMapping("/{categoryName}")
    public ResponseEntity<SuccessResponse> getCategory(@PathVariable String categoryName) {
        CategoryDto category = categoryService.getCategoryByName(categoryName);
        return SuccessResponse.setSuccessResponseOk("Category Fetched Successfully", Map.of("category", category));
    }

    /**
     * <p>
     * Get all the categories
     * </p>
     *
     * @param page Page number
     * @param size Number of categories per page
     * @return {@link SuccessResponse} with the list of categories and {@link HttpStatus} OK
     */
    @GetMapping
    public ResponseEntity<SuccessResponse> getCategories(@RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "10") int size) {
        Set<CategoryDto> categories = categoryService.getAllCategories(page, size);
        return SuccessResponse.setSuccessResponseOk("Categories Fetched Successfully", Map.of("categories", categories));
    }

    /**
     * <p>
     * Update the category
     * </p>
     *
     * @param categoryDto Category to be updated
     * @return {@link SuccessResponse} with the updated category and {@link HttpStatus} OK
     */
    @PatchMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<SuccessResponse> updateCategory(@RequestBody CategoryDto categoryDto) {
        CategoryDto category = categoryService.updateCategory(categoryDto);
        return SuccessResponse.setSuccessResponseOk("Category updated Successfully", Map.of("category", category));
    }

    /**
     * <p>
     * Delete the category by name
     * </p>
     *
     * @param categoryName Name of the category
     * @return {@link SuccessResponse} with {@link HttpStatus} NO_CONTENT
     */
    @DeleteMapping("/{categoryName}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<SuccessResponse> deleteCategory(@PathVariable String categoryName) {
        categoryService.deleteCategory(categoryName);
        return SuccessResponse.setSuccessResponseNoContent("Category Deleted Successfully");
    }
}
