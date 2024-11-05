package com.bicart.controller;

import com.bicart.dto.CategoryDto;
import com.bicart.model.Category;
import com.bicart.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/v1/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<CategoryDto> addCategory(@RequestBody CategoryDto category) {
        return new ResponseEntity<>(categoryService.addCategory(category), HttpStatus.CREATED);
    }

    @GetMapping("/{categoryName}")
    public ResponseEntity<Category> getCategory(@PathVariable String categoryName) {
        return new ResponseEntity<>(categoryService.getCategoryByName(categoryName), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Set<CategoryDto>> getCategories(@RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "10") int size) {
        return new ResponseEntity<>(categoryService.getAllCategories(page, size), HttpStatus.OK);
    }

    @PatchMapping
    @PreAuthorize("hasRole('ADMIN')")
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
