package com.bicart.service;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DuplicateKeyException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.bicart.dto.CategoryDto;
import com.bicart.model.Category;
import com.bicart.repository.CategoryRepository;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @InjectMocks
    private CategoryService categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    private Category category;
    private CategoryDto categoryDto;

    @BeforeEach
    void setUp() {
        category = Category.builder()
                .id("1")
                .name("testCategory")
                .description("testDescription")
                .build();

        categoryDto = CategoryDto.builder()
                .id("1")
                .name("testCategory")
                .description("testDescription")
                .build();
    }

    @Test
    void testGetCategoryModelByName() {
        when(categoryRepository.findByNameAndIsDeletedFalse(anyString())).thenReturn(category);
        Category result = categoryRepository.findByNameAndIsDeletedFalse("1");
        assertNotNull(result);
        assertEquals(result.getName(), category.getName());
    }

    @Test
    void testGetAllCategories() {
        when(categoryRepository.findAllByIsDeletedFalse(any())).thenReturn(List.of(category));
        assertEquals(1, categoryService.getAllCategories(0,1).size());
    }

    @Test
    void testAddCategorySuccess() {
        when(categoryRepository.save(any(Category.class))).thenReturn(null);
        categoryService.addCategory(categoryDto);
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void testAddCategoryThrowsDuplicateKeyException(){
        when(categoryRepository.existsByName(anyString())).thenReturn(true);
        assertThrows(DuplicateKeyException.class, () -> categoryService.addCategory(categoryDto));
    }

    @Test
    void testUpdateCategorySuccess() {
        when(categoryRepository.save(any(Category.class))).thenReturn(category);
        when(categoryRepository.findByNameAndIsDeletedFalse(anyString())).thenReturn(category);
        CategoryDto result = categoryService.updateCategory(categoryDto);
        assertNotNull(result);
        assertEquals(categoryDto.getName(), result.getName());
    }

    @Test
    void testDeleteCategory() {
        when(categoryRepository.findByNameAndIsDeletedFalse(anyString())).thenReturn(category);
        when(categoryRepository.save(any(Category.class))).thenReturn(null);
        categoryService.deleteCategory("testName");
    }

}