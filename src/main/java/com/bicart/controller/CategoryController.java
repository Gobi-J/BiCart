package com.bicart.controller;

import java.util.Set;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
 *     CategoryController class is a REST controller class that handles all the category related requests.
 * </p>
 */
@RestController
@RequestMapping("/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * <p>
     *     Add a new category
     * </p>
     * @param category Category to be added
     * @return {@link ResponseEntity} with the added category and {@link HttpStatus} CREATED
     */
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<CategoryDto> addCategory(@RequestBody CategoryDto category) {
        return new ResponseEntity<>(categoryService.addCategory(category), HttpStatus.CREATED);
    }

    /**
     * <p>
     *     Get the category by name
     * </p>
     * @param categoryName Name of the category
     * @return {@link ResponseEntity} with the category and {@link HttpStatus} OK
     */
    @GetMapping("/{categoryName}")
    public ResponseEntity<Category> getCategory(@PathVariable String categoryName) {
        return new ResponseEntity<>(categoryService.getCategoryByName(categoryName), HttpStatus.OK);
    }

    /**
     * <p>
     *     Get all the categories
     * </p>
     * @param page Page number
     * @param size Number of categories per page
     * @return {@link ResponseEntity} with the list of categories and {@link HttpStatus} OK
     */
    @GetMapping
    public ResponseEntity<Set<CategoryDto>> getCategories(@RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "10") int size) {
        return new ResponseEntity<>(categoryService.getAllCategories(page, size), HttpStatus.OK);
    }

    /**
     * <p>
     *   Update the category
     * </p>
     * @param categoryDto Category to be updated
     * @return {@link ResponseEntity} with the updated category and {@link HttpStatus} OK
     */
    @PatchMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<CategoryDto> updateCategory(@RequestBody CategoryDto categoryDto) {
        return new ResponseEntity<>(categoryService.updateCategory(categoryDto), HttpStatus.OK);
    }

    /**
     * <p>
     *     Delete the category by name
     * </p>
     * @param categoryName Name of the category
     * @return {@link ResponseEntity} with {@link HttpStatus} NO_CONTENT
     */
    @DeleteMapping("/{categoryName}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<HttpStatus> deleteCategory(@PathVariable String categoryName) {
        categoryService.deleteCategory(categoryName);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
