package com.bicart.controller;

import java.util.Set;

import com.bicart.helper.SuccessResponse;
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
import com.bicart.model.Category;
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
     * @return {@link ResponseEntity} with the added category and {@link HttpStatus} CREATED
     */
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<SuccessResponse> addCategory(@Validated @RequestBody CategoryDto category) {
        categoryService.addCategory(category);
        return SuccessResponse.setSuccessResponse("Category Added Successfully", HttpStatus.CREATED);
    }

    /**
     * <p>
     * Get the category by name
     * </p>
     *
     * @param categoryName Name of the category
     * @return {@link ResponseEntity} with the category and {@link HttpStatus} OK
     */
    @GetMapping("/{categoryName}")
    public ResponseEntity<SuccessResponse> getCategory(@PathVariable String categoryName) {
        CategoryDto categoryDto = categoryService.getCategoryByName(categoryName);
        return SuccessResponse.setSuccessResponse("Category Fetched Successfully", HttpStatus.OK, categoryDto);
    }

    /**
     * <p>
     * Get all the categories
     * </p>
     *
     * @param page Page number
     * @param size Number of categories per page
     * @return {@link ResponseEntity} with the list of categories and {@link HttpStatus} OK
     */
    @GetMapping
    public ResponseEntity<SuccessResponse> getCategories(@RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "10") int size) {
        Set<CategoryDto> categoriesDto = categoryService.getAllCategories(page, size);
        return SuccessResponse.setSuccessResponse("Categories Fetched Successfully", HttpStatus.OK, categoriesDto);
    }

    /**
     * <p>
     * Update the category
     * </p>
     *
     * @param categoryDto Category to be updated
     * @return {@link ResponseEntity} with the updated category and {@link HttpStatus} OK
     */
    @PatchMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<SuccessResponse> updateCategory(@RequestBody CategoryDto categoryDto) {
        CategoryDto category = categoryService.updateCategory(categoryDto);
        return SuccessResponse.setSuccessResponse("Category updated Successfully", HttpStatus.OK, category);
    }

    /**
     * <p>
     * Delete the category by name
     * </p>
     *
     * @param categoryName Name of the category
     * @return {@link ResponseEntity} with {@link HttpStatus} NO_CONTENT
     */
    @DeleteMapping("/{categoryName}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<SuccessResponse> deleteCategory(@PathVariable String categoryName) {
        categoryService.deleteCategory(categoryName);
        return SuccessResponse.setSuccessResponse("Category Deleted Successfully", HttpStatus.NO_CONTENT);
    }
}
