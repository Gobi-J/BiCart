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
    public ResponseEntity<SubCategoryDto> addSubCategory(@RequestBody SubCategoryDto subCategory) {
        return new ResponseEntity<>(subCategoryService.addSubCategory(subCategory), HttpStatus.CREATED);
    }

    @GetMapping("/{subCategoryName}")
    public ResponseEntity<SubCategory> getSubCategory(@PathVariable String subCategoryName) {
        return new ResponseEntity<>(subCategoryService.getSubCategoryByName(subCategoryName), HttpStatus.OK);
    }

    @GetMapping
<<<<<<< HEAD
    public ResponseEntity<Set<SubCategoryDto>> getCategories(@RequestParam(defaultValue = "0") int page,
=======
    public ResponseEntity<Set<SubCategoryDto>> getSubCategories(@RequestParam(defaultValue = "0") int page,
>>>>>>> ae649a1 (FEAT: Added controllers)
                                                             @RequestParam(defaultValue = "10") int size) {
        return new ResponseEntity<>(subCategoryService.getSubCategories(page, size), HttpStatus.OK);
    }

    @PatchMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SubCategoryDto> updateSubCategory(@RequestBody SubCategoryDto categoryDto) {
        return new ResponseEntity<>(subCategoryService.updateSubCategory(categoryDto), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{subCategoryName}")
    public ResponseEntity<String> deleteSubCategory(@PathVariable String subCategoryName) {
        subCategoryService.deleteSubCategory(subCategoryName);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
