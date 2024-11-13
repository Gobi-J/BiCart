package com.bicart.controller;

import java.util.Map;
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

import com.bicart.dto.SubCategoryDto;
import com.bicart.helper.SuccessResponse;
import com.bicart.service.SubCategoryService;

/**
 * <p>
 * SubCategoryController class is a REST controller class that handles all the sub category related requests.
 * </p>
 */
@RestController
@RequestMapping("/v1/sub-categories")
@RequiredArgsConstructor
public class SubCategoryController {

    private final SubCategoryService subCategoryService;

    /**
     * <p>
     * Adds a new sub category to the database.
     * </p>
     *
     * @param subCategory details to be added.
     * @return {@link SuccessResponse} with {@link HttpStatus} CREATED
     */
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<SuccessResponse> addSubCategory(@RequestBody SubCategoryDto subCategory) {
        subCategoryService.addSubCategory(subCategory);
        return SuccessResponse.setSuccessResponse("SubCategory added Successfully", HttpStatus.CREATED);
    }

    /**
     * <p>
     * Gets a sub category by its name.
     * </p>
     *
     * @param subCategoryName for which subcategory is fetched
     * @return {@link SubCategoryDto} with {@link HttpStatus} OK
     */
    @GetMapping("/{subCategoryName}")
    public ResponseEntity<SuccessResponse> getSubCategory(@PathVariable String subCategoryName) {
        SubCategoryDto subCategory = subCategoryService.getSubCategoryByName(subCategoryName);
        return SuccessResponse.setSuccessResponse("SubCategory fetched Successfully", HttpStatus.OK, Map.of("subCategory", subCategory));
    }

    /**
     * <p>
     * Gets all the sub categories.
     * </p>
     *
     * @param page page number
     * @param size number of sub categories per page
     * @return {@link SuccessResponse} containing the list of sub categories, with {@link HttpStatus} OK
     */
    @GetMapping
    public ResponseEntity<SuccessResponse> getSubCategories(@RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "10") int size) {
        Set<SubCategoryDto> subCategories = subCategoryService.getSubCategories(page, size);
        return SuccessResponse.setSuccessResponse("SubCategories fetched Successfully", HttpStatus.OK, Map.of("subCategories", subCategories));
    }

    /**
     * <p>
     * Updates the sub category details.
     * </p>
     *
     * @param subCategory The subcategory details to be updated.
     * @return {@link SuccessResponse} containing the updated subcategory details, with {@link HttpStatus} OK
     */
    @PatchMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<SuccessResponse> updateSubCategory(@RequestBody SubCategoryDto subCategory) {
        subCategory = subCategoryService.updateSubCategory(subCategory);
        return SuccessResponse.setSuccessResponse("SubCategory Updated Successfully", HttpStatus.OK, Map.of("subCategory", subCategory));
    }

    /**
     * <p>
     * Deletes a sub category.
     * </p>
     *
     * @param subCategoryName for which subcategory is deleted
     * @return {@link SuccessResponse} with {@link HttpStatus} NO_CONTENT
     */
    @DeleteMapping("/{subCategoryName}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<SuccessResponse> deleteSubCategory(@PathVariable String subCategoryName) {
        subCategoryService.deleteSubCategory(subCategoryName);
        return SuccessResponse.setSuccessResponse("SubCategory deleted Successfully", HttpStatus.NO_CONTENT);

    }
}
