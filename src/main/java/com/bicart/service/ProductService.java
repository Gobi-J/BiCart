package com.bicart.service;

import com.bicart.dto.ProductDto;
import com.bicart.helper.CustomException;
import com.bicart.mapper.ProductMapper;
import com.bicart.model.Product;
import com.bicart.repository.ProductRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

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
     *     Save product in database
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
            throw new CustomException("Error while saving product");
        }
    }

    /**
     * <p>
     *     Add product to database
     * </p>
     *
     * @param productDto object to be added
     * @return {@link ProductDto} added object
     * @throws CustomException if error occurs while adding product
     */
    public ProductDto addProduct(@NonNull ProductDto productDto) {
        logger.debug("Adding product with name: {} ", productDto.getName());
        try {
            Product product = ProductMapper.dtoToModel(productDto);
            product.setId(UUID.randomUUID().toString());
            product.setCreatedAt(new Date());
            product.setIsDeleted(false);
            product.setSubCategory(subCategoryService.getSubCategoryByName(productDto.getSubCategory().getName()));
            return ProductMapper.modelToDto(saveProduct(product));
        } catch (Exception e) {
            logger.error("Error in adding the product with the Id: {}", productDto.getId(), e);
            throw new CustomException("Error while adding product");
        }
    }

    /**
     * <p>
     *     Get product by id
     * </p>
     *
     * @param id of product to be fetched
     * @return {@link Product} fetched object
     * @throws CustomException if error occurs while getting product
     */
    public Product getProductById(@NonNull String id) {
        logger.debug("Getting product with id: {}", id);
        try {
            Product product = productRepository.findByIdAndIsDeletedFalse(id);
            if (product == null) {
                throw new NoSuchElementException("Product not found for the Id: " + id);
            }
            return product;
        } catch (Exception e) {
            if (e instanceof NoSuchElementException) {
                logger.warn("Product not found for the Id: {} ", id, e);
                throw e;
            }
            logger.error(e);
            throw new CustomException("Error while getting product");
        }
    }

    /**
     * <p>
     *     Get all products
     * </p>
     *
     * @param page number of page
     * @param size number of products per page
     * @return {@link Set<ProductDto>} of all products
     * @throws CustomException if error occurs while getting all products
     */
    public Set<ProductDto> getAllProducts(int page, int size) {
        logger.debug("Getting all products");
        try {
            Pageable pageable = PageRequest.of(page, size);
            return productRepository.findAllByIsDeletedFalse(pageable).stream()
                    .map(ProductMapper::modelToDto)
                    .collect(Collectors.toSet());
        } catch (Exception e) {
            logger.error("Error in retrieving the products for the page : {}", page, e);
            throw new CustomException("Error while getting all products");
        }
    }

    /**
     * <p>
     *     Delete product by id
     * </p>
     *
     * @param id of product to be deleted
     * @throws CustomException if error occurs while deleting product
     */
    public void deleteProduct(@NonNull String id) {
        logger.debug("Deleting product with id: " + id);
        try {
            Product product = productRepository.findByIdAndIsDeletedFalse(id);
            if (product == null) {
                throw new NoSuchElementException("Product not found for the Id: " + id);
            }
            product.setIsDeleted(true);
            saveProduct(product);
        } catch (Exception e) {
            if (e instanceof NoSuchElementException) {
                logger.warn("Product not found for the Id:{} ", id, e);
                throw e;
            }
            logger.error("Error in deleting the product for the Id:{} ", id, e);
            throw new CustomException("Error while deleting product");
        }
    }

    /**
     * <p>
     *     Update product
     * </p>
     *
     * @param productDto object to be updated
     * @return {@link ProductDto} updated object
     * @throws CustomException if error occurs while updating product
     */
    public ProductDto updateProduct(@NonNull ProductDto productDto) {
        logger.debug("Updating product with id: {} ", productDto.getId());
        try {
            Product product = ProductMapper.dtoToModel(productDto);
            if (!productRepository.existsByName(productDto.getName())) {
                throw new NoSuchElementException("Product not found for the Id: {}" + productDto.getId());
            }
            product.setUpdatedAt(new Date());
            product.setSubCategory(subCategoryService.getSubCategoryByName(productDto.getSubCategory().getName()));
            return ProductMapper.modelToDto(saveProduct(product));
        } catch (Exception e) {
            if (e instanceof NoSuchElementException) {
                logger.warn("Product not found for the Id: {}", productDto.getId(), e);
                throw e;
            }
            logger.error("Error in updating the product with the Id:{}", productDto.getId(), e);
            throw new CustomException("Error while updating product");
        }
    }

    /**
     * <p>
     *     Update category of product
     * </p>
     *
     * @param productId of product to be updated
     * @param subCategoryName of product to be updated
     * @throws CustomException if error occurs while updating category of product
     */
    public void updateCategory(@NonNull String productId, @NonNull String subCategoryName) {
        logger.debug("Updating category for product with id: " + productId);
        try {
            Product product = productRepository.findByIdAndIsDeletedFalse(productId);
            if (product == null) {
                throw new NoSuchElementException("Product not found for the Id: {}" + productId);
            }
            product.setSubCategory(subCategoryService.getSubCategoryByName(subCategoryName));
            ProductMapper.modelToDto(saveProduct(product));
        } catch (Exception e) {
            if (e instanceof NoSuchElementException) {
                logger.warn("Product not found for the Id: {}", productId, e);
                throw e;
            }
            logger.error("Error in updating the product's category with the Id:{}", productId, e);
            throw new CustomException("Error while updating category for product");
        }
    }

    /**
     * <p>
     *     Get products by sub category name
     * </p>
     *
     * @param subCategoryName of products to be fetched
     * @param page number of page
     * @param size number of products per page
     * @return {@link Set<ProductDto>} of products
     * @throws CustomException if error occurs while getting products by sub category name
     */
    public Set<ProductDto> getProductsBySubCategoryName(@NonNull String subCategoryName, int page, int size) {
        logger.debug("Getting products by subCategoryName: " + subCategoryName);
        try {
            Pageable pageable = PageRequest.of(page, size);
            return productRepository.findAllBySubCategoryNameAndIsDeletedFalse(subCategoryName, pageable).stream()
                    .map(ProductMapper::modelToDto)
                    .collect(Collectors.toSet());
        } catch (Exception e) {
            logger.error("Error in retrieving the products", e);
            throw new CustomException("Error while getting products by subCategoryName");
        }
    }
}
