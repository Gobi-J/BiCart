package com.bicart.controller;

import com.bicart.dto.CategoryDto;
import com.bicart.dto.SubCategoryDto;
import com.bicart.model.SubCategory;
import com.bicart.service.SubCategoryService;
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

import java.util.Set;

/**
 * <p>
 *     SubCategoryController class is a REST controller class that handles all the sub category related requests.
 * </p>
 */
@RestController
@RequestMapping("/v1/sub-categories")
@RequiredArgsConstructor
public class SubCategoryController {

    private final SubCategoryService subCategoryService;

    /**
     * <p>
     *     Adds a new sub category to the database.
     * </p>
     * @param subCategory The sub category details to be added.
     * @return {@link ResponseEntity<SubCategoryDto>} sub category details that were added with {@link HttpStatus} CREATED
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<SubCategoryDto> addSubCategory(@RequestBody SubCategoryDto subCategory) {
        return new ResponseEntity<>(subCategoryService.addSubCategory(subCategory), HttpStatus.CREATED);
    }

    /**
     * <p>
     *     Gets a sub category by its name.
     * </p>
     * @param subCategoryName for which sub category is fetched
     * @return {@link ResponseEntity<SubCategory>} sub category with {@link HttpStatus} OK
     */
    @GetMapping("/{subCategoryName}")
    public ResponseEntity<SubCategory> getSubCategory(@PathVariable String subCategoryName) {
        return new ResponseEntity<>(subCategoryService.getSubCategoryByName(subCategoryName), HttpStatus.OK);
    }

    /**
     * <p>
     *     Gets all the sub categories.
     * </p>
     * @param page page number
     * @param size number of sub categories per page
     * @return {@link ResponseEntity<Set<SubCategoryDto>} sub categories with {@link HttpStatus} OK
     */
    @GetMapping
    public ResponseEntity<Set<SubCategoryDto>> getSubCategories(@RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "10") int size) {
        return new ResponseEntity<>(subCategoryService.getSubCategories(page, size), HttpStatus.OK);
    }

    /**
     * <p>
     *     Updates the sub category details.
     * </p>
     * @param categoryDto The sub category details to be updated.
     * @return {@link ResponseEntity<SubCategoryDto>} sub category details that were updated with {@link HttpStatus} OK
     */
    @PatchMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SubCategoryDto> updateSubCategory(@RequestBody SubCategoryDto categoryDto) {
        return new ResponseEntity<>(subCategoryService.updateSubCategory(categoryDto), HttpStatus.OK);
    }

    /**
     * <p>
     *     Deletes a sub category.
     * </p>
     * @param subCategoryName for which sub category is deleted
     * @return {@link HttpStatus} NO_CONTENT if sub category is deleted
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{subCategoryName}")
    public ResponseEntity<String> deleteSubCategory(@PathVariable String subCategoryName) {
        subCategoryService.deleteSubCategory(subCategoryName);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
