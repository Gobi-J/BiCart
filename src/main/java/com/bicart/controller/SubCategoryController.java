package com.bicart.controller;

import com.bicart.dto.CategoryDto;
import com.bicart.dto.SubCategoryDto;
import com.bicart.model.Category;
import com.bicart.model.SubCategory;
import com.bicart.service.CategoryService;
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

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/v1/sub-categories")
public class SubCategoryController {

    private final SubCategoryService subCategoryService;

    public SubCategoryController(SubCategoryService subCategoryService) {
        this.subCategoryService = subCategoryService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<SubCategoryDto> addCategory(@RequestBody SubCategoryDto subCategory) {
        return new ResponseEntity<>(subCategoryService.addSubCategory(subCategory), HttpStatus.CREATED);
    }

    @GetMapping("/{subCategoryName}")
    public ResponseEntity<SubCategory> getCategory(@PathVariable String subCategoryName) {
        return new ResponseEntity<>(subCategoryService.getSubCategoryByName(subCategoryName), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Set<SubCategoryDto>> getCategories(@RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "10") int size) {
        return new ResponseEntity<>(subCategoryService.getSubCategories(page, size), HttpStatus.OK);
    }

    @PatchMapping
    public ResponseEntity<CategoryDto> updateCategory(@RequestBody CategoryDto categoryDto) {
        return new ResponseEntity<>(categoryService.updateCategory(categoryDto), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{categoryName}")
    public ResponseEntity<String> deleteCategory(@PathVariable String categoryName) {
        categoryService.deleteCategory(categoryName);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
