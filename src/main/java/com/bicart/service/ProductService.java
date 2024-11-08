package com.bicart.service;

import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.bicart.dto.ProductDto;
import com.bicart.helper.CustomException;
import com.bicart.mapper.ProductMapper;
import com.bicart.model.Product;
import com.bicart.repository.ProductRepository;

/**
 * <p>
 * Service class that handles business logic related to products.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final SubCategoryService subCategoryService;

    private final static Logger logger = LogManager.getLogger(ProductService.class);

    /**
     * <p>
     * Save product in database
     * </p>
     *
     * @param product object to be saved
     * @return {@link Product} saved object
     * @throws CustomException if error occurs while saving product
     */
    public Product saveProduct(Product product) {
        try {
            return productRepository.save(product);
        } catch (Exception e) {
            logger.error("Error in saving the product with the id : {} ", product.getId(), e);
            throw new CustomException("Cannot save product");
        }
    }

    /**
     * <p>
     * Add product to database
     * </p>
     *
     * @param productDto object to be added
     * @throws CustomException if error occurs while adding product
     */
    public void addProduct(@NonNull ProductDto productDto) {
        logger.debug("Adding product with name: {} ", productDto.getName());
        Product product = ProductMapper.dtoToModel(productDto);
        product.setId(UUID.randomUUID().toString());
        product.setAudit("ADMIN");
        product.setSubCategory(subCategoryService.getSubCategoryModelByName(productDto.getSubCategory().getName()));
        ProductMapper.modelToDto(saveProduct(product));
    }

    /**
     * <p>
     * Get product by id
     * </p>
     *
     * @param id of product to be fetched
     * @return {@link Product} fetched object
     * @throws CustomException if error occurs while getting product
     */
    public ProductDto getProductById(@NonNull String id) {
        Product product = getProductModelById(id);
        return ProductMapper.modelToDto(product);
    }

    /**
     * <p>
     * Get product by id
     * </p>
     *
     * @param id of product to be fetched
     * @return {@link Product} fetched object
     * @throws CustomException if error occurs while getting product
     */
    public Product getProductModelById(@NonNull String id) {
        Product product = productRepository.findByIdAndIsDeletedFalse(id);
        if (product == null) {
            logger.warn("Product not found for the Id: {}", id);
            throw new NoSuchElementException("Product not found for the Id: " + id);
        }
        return product;
    }

    /**
     * <p>
     * Get all products
     * </p>
     *
     * @param page number of page
     * @param size number of products per page
     * @return {@link Set<ProductDto>} of all products
     * @throws CustomException if error occurs while getting all products
     */
    public Set<ProductDto> getAllProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productRepository.findAllByIsDeletedFalse(pageable).stream()
                .map(ProductMapper::modelToDto)
                .collect(Collectors.toSet());
    }

    /**
     * <p>
     * Delete product by id
     * </p>
     *
     * @param id of product to be deleted
     * @throws CustomException if error occurs while deleting product
     */
    public void deleteProduct(@NonNull String id) {
        Product product = getProductModelById(id);
        product.setIsDeleted(true);
        saveProduct(product);
    }

    /**
     * <p>
     * Update product
     * </p>
     *
     * @param productDto object to be updated
     * @return {@link ProductDto} updated object
     * @throws CustomException if error occurs while updating product
     */
    public ProductDto updateProduct(@NonNull ProductDto productDto) {
        Product product = ProductMapper.dtoToModel(productDto);
        if (!productRepository.existsByName(productDto.getName())) {
            throw new NoSuchElementException("Product not found for the Id: {}" + product.getId());
        }
        product.setUpdatedAt(new Date());
        product.setSubCategory(subCategoryService.getSubCategoryModelByName(productDto.getSubCategory().getName()));
        return ProductMapper.modelToDto(saveProduct(product));
    }

    /**
     * <p>
     * Update category of product
     * </p>
     *
     * @param productId       of product to be updated
     * @param subCategoryName of product to be updated
     * @throws CustomException if error occurs while updating category of product
     */
    public void updateCategory(@NonNull String productId, @NonNull String subCategoryName) {
        Product product = getProductModelById(productId);
        product.setSubCategory(subCategoryService.getSubCategoryModelByName(subCategoryName));
        ProductMapper.modelToDto(saveProduct(product));
    }

    /**
     * <p>
     * Get products by sub category name
     * </p>
     *
     * @param subCategoryName of products to be fetched
     * @param page            number of page
     * @param size            number of products per page
     * @return {@link Set<ProductDto>} of products
     * @throws CustomException if error occurs while getting products by sub category name
     */
    public Set<ProductDto> getProductsBySubCategoryName(@NonNull String subCategoryName, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productRepository.findAllBySubCategoryNameAndIsDeletedFalse(subCategoryName, pageable).stream()
                .map(ProductMapper::modelToDto)
                .collect(Collectors.toSet());
    }
}
