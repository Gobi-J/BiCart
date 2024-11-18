package com.bicart.service;

import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.bicart.dto.ProductDto;
import com.bicart.helper.BiCartException;
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
     * @throws BiCartException if error occurs while saving product
     */
    protected Product saveProduct(Product product) {
        try {
            return productRepository.save(product);
        } catch (Exception e) {
            logger.error("Error in saving the product with the id : {} ", product.getId(), e);
            throw new BiCartException("Cannot save product");
        }
    }

    /**
     * <p>
     * Add product to database
     * </p>
     *
     * @param productDto object to be added
     */
    public void addProduct(@NonNull ProductDto productDto) {
        logger.debug("Adding product with name: {} ", productDto.getName());
        try {
            Product product = ProductMapper.dtoToModel(productDto);
            product.setAudit("ADMIN");
            product.setSubCategory(subCategoryService.getSubCategoryModelByName(productDto.getSubCategory().getName()));
            saveProduct(product);
        } catch (Exception e) {
            logger.error("Error in adding product with name: {} ", productDto.getName(), e);
            throw new BiCartException(e.getMessage());
        }
    }

    /**
     * <p>
     * Get product by id
     * </p>
     *
     * @param id of product to be fetched
     * @return {@link Product} which is retrieved
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
     * @throws BiCartException if error occurs while getting product
     */
    protected Product getProductModelById(@NonNull String id) {
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
     * @param page page number to be fetched
     * @param size number of products per page
     * @return {@link ProductDto} set containing of all products
     */
    public Set<ProductDto> getAllProducts(int page, int size, String subCategoryName) {
        Pageable pageable = PageRequest.of(page, size);
        if (!subCategoryName.equals("all")) {
            return getProductsBySubCategoryName(subCategoryName, page, size);
        }
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
     * @throws NoSuchElementException if the product is not found
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
     */
    public void updateCategory(@NonNull String productId, @NonNull String subCategoryName) {
        Product product = getProductModelById(productId);
        product.setSubCategory(subCategoryService.getSubCategoryModelByName(subCategoryName));
        saveProduct(product);
    }

    /**
     * <p>
     * Get products by sub category name
     * </p>
     *
     * @param subCategoryName of products to be fetched
     * @param page            page number to be fetched
     * @param size            number of products per page
     * @return {@link ProductDto} set containing details of products
     */
    public Set<ProductDto> getProductsBySubCategoryName(@NonNull String subCategoryName, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productRepository.findAllBySubCategoryNameAndIsDeletedFalse(subCategoryName, pageable).stream()
                .map(ProductMapper::modelToDto)
                .collect(Collectors.toSet());
    }
}
