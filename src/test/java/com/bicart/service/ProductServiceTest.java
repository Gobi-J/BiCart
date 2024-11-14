package com.bicart.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

import com.bicart.dto.CategoryDto;
import com.bicart.dto.ProductDto;
import com.bicart.dto.SubCategoryDto;
import com.bicart.model.Category;
import com.bicart.model.Product;
import com.bicart.model.SubCategory;
import com.bicart.repository.ProductRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    private Product product;
    private ProductDto productDto;
    private SubCategory subCategory;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private SubCategoryService subCategoryService;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() {
        Category category = Category.builder()
                .id("1")
                .name("test name")
                .description("test")
                .build();

        CategoryDto categoryDto = CategoryDto.builder()
                .id("1")
                .name("test name")
                .description("test")
                .build();

        subCategory = SubCategory.builder()
                .id("1")
                .name("Sample SubCategory")
                .category(category)
                .build();

        SubCategoryDto subCategoryDto = SubCategoryDto.builder()
                .id("1")
                .name("Sample SubCategory")
                .category(categoryDto)
                .build();

        productDto = ProductDto.builder()
                .id("1")
                .name("Sample Product")
                .description("Product description")
                .price(100)
                .subCategory(subCategoryDto)
                .build();

        product = Product.builder()
                .id("1")
                .name("Sample Product")
                .description("Product description")
                .subCategory(subCategory)
                .price(100)
                .build();
    }

    @Test
    void testAddProductSuccess() {
        when(subCategoryService.getSubCategoryModelByName(anyString())).thenReturn(subCategory);
        when(productRepository.save(any(Product.class))).thenReturn(product);
        productService.addProduct(productDto);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testGetProductByIdSuccess() {
        when(productRepository.findByIdAndIsDeletedFalse(anyString())).thenReturn(product);
        ProductDto productDto = productService.getProductById("1");
        assertNotNull(productDto);
        assertEquals(productDto.getName(), product.getName());
    }

    @Test
    void testGetProductByIdFailure() {
        when(productRepository.findByIdAndIsDeletedFalse(anyString())).thenReturn(null);
        assertThrows(NoSuchElementException.class, () -> productService.getProductById("1"));
    }

    @Test
    void testGetAllProductsSuccess() {
        when(productRepository.findAllByIsDeletedFalse(PageRequest.of(0, 1))).thenReturn(List.of(product));
        assertEquals(1, productService.getAllProducts(0, 1).size());
    }

    @Test
    void testDeleteProductSuccess() {
        when(productRepository.findByIdAndIsDeletedFalse(anyString())).thenReturn(product);
        when(productRepository.save(product)).thenReturn(product);
        productService.deleteProduct("1");
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void testUpdateProductSuccess() {
        when(productRepository.existsByName(anyString())).thenReturn(true);
        when(subCategoryService.getSubCategoryModelByName(anyString())).thenReturn(subCategory);
        when(productRepository.save(any(Product.class))).thenReturn(product);
        ProductDto result = productService.updateProduct(productDto);
        assertNotNull(result);
        assertEquals(productDto.getName(), result.getName());
    }

    @Test
    void testUpdateProductNotExists() {
        when(productRepository.existsByName(anyString())).thenReturn(false);
        assertThrows(NoSuchElementException.class, () -> productService.updateProduct(productDto));
    }

    @Test
    void testUpdateCategorySuccess() {
        when(productRepository.findByIdAndIsDeletedFalse(anyString())).thenReturn(product);
        when(subCategoryService.getSubCategoryModelByName(anyString())).thenReturn(subCategory);
        when(productRepository.save(product)).thenReturn(product);
        productService.updateCategory("1", "category");
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void testGetProductsBySubCategoryNameSuccess() {
        when(productRepository.findAllBySubCategoryNameAndIsDeletedFalse(anyString(), any())).thenReturn(List.of(product));
        assertEquals(1, productService.getProductsBySubCategoryName("test", 0, 1).size());
    }
}