package com.bicart.service;

import com.bicart.dto.AddressDto;
import com.bicart.dto.CartDto;
import com.bicart.dto.CategoryDto;
import com.bicart.helper.CustomException;
import com.bicart.model.Address;
import com.bicart.model.Cart;
import com.bicart.model.Category;
import com.bicart.repository.CartRepository;
import com.bicart.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

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