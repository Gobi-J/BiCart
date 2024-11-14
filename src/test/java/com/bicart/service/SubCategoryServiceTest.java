package com.bicart.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.PageRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.bicart.dto.CategoryDto;
import com.bicart.dto.SubCategoryDto;
import com.bicart.model.Category;
import com.bicart.model.SubCategory;
import com.bicart.repository.SubCategoryRepository;

@ExtendWith(MockitoExtension.class)
class SubCategoryServiceTest {

    private SubCategoryDto subCategoryDto;
    private SubCategory subCategory;
    private Category category;

    @Mock
    private SubCategoryRepository subCategoryRepository;

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private SubCategoryService subCategoryService;

    @BeforeEach
    void setUp() {
        category = Category.builder()
            .id("1")
            .name("Electronics")
            .description("Electronic items")
            .build();

        subCategory = SubCategory.builder()
            .id("1")
            .name("Mobile")
            .description("Mobile")
            .category(category)
            .build();

        CategoryDto categoryDto = CategoryDto.builder()
                .id("1")
                .name("Electronics")
                .description("Electronic items")
                .build();

        subCategoryDto = SubCategoryDto.builder()
                .id("1")
                .name("Mobile")
                .description("Mobile")
                .category(categoryDto)
                .build();
    }

    @Test
    void testGetSubCategoryByNameSuccess() {
        when(subCategoryRepository.findByNameAndIsDeletedFalse(anyString())).thenReturn(subCategory);
        SubCategoryDto result = subCategoryService.getSubCategoryByName("Mobile");
        assertNotNull(subCategoryDto);
        assertEquals(result.getName(), subCategoryDto.getName());
    }

    @Test
    void testGetSubCategoryByNameNotExists() {
        when(subCategoryRepository.findByNameAndIsDeletedFalse(anyString())).thenReturn(null);
        assertThrows(NoSuchElementException.class, () -> subCategoryService.getSubCategoryByName("Electronics"));
    }

    @Test
    void testGetSubCategoriesSuccess() {
        when(subCategoryRepository.findAllByIsDeletedFalse(PageRequest.of(0, 1))).thenReturn(List.of(subCategory));
        Set<SubCategoryDto> result = subCategoryService.getSubCategories(0, 1);
        assertNotNull(result);
        assertEquals(result.size(), 1);
    }

    @Test
    void testAddSubCategorySuccess() {
        when(subCategoryRepository.existsByName(anyString())).thenReturn(false);
        when(categoryService.getCategoryModelByName(anyString())).thenReturn(category);
        when(subCategoryRepository.save(any(SubCategory.class))).thenReturn(subCategory);
        subCategoryService.addSubCategory(subCategoryDto);
        verify(subCategoryRepository, times(1)).save(any(SubCategory.class));
    }

    @Test
    void testAddSubCategoryExists() {
        when(subCategoryRepository.existsByName(anyString())).thenReturn(true);
        assertThrows(DuplicateKeyException.class, () -> subCategoryService.addSubCategory(subCategoryDto));
    }

    @Test
    void testUpdateSubCategorySuccess() {
        when(subCategoryRepository.findByNameAndIsDeletedFalse(anyString())).thenReturn(subCategory);
        when(categoryService.getCategoryModelByName(anyString())).thenReturn(category);
        when(subCategoryRepository.save(any(SubCategory.class))).thenReturn(subCategory);
        subCategoryService.updateSubCategory(subCategoryDto);
        verify(subCategoryRepository, times(1)).save(any(SubCategory.class));
    }

    @Test
    void testDeleteSubCategorySuccess() {
        when(subCategoryRepository.findByNameAndIsDeletedFalse(anyString())).thenReturn(subCategory);
        when(subCategoryRepository.save(any(SubCategory.class))).thenReturn(subCategory);
        subCategoryService.deleteSubCategory("Mobile");
        verify(subCategoryRepository, times(1)).save(any(SubCategory.class));
    }
}