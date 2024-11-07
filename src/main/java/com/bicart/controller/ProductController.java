package com.bicart.controller;

import java.util.Set;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bicart.dto.ProductDto;
import com.bicart.dto.ReviewDto;
import com.bicart.model.Product;
import com.bicart.service.ProductService;
import com.bicart.service.ReviewService;

/**
 * <p>
 *     ProductController class is a REST controller that handles all the requests related to products.
 * </p>
 */
@RestController
@RequestMapping("/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ReviewService reviewService;

    /**
     * <p>
     *     Adds a new product to the database.
     * </p>
     * @param productDto The product details to be added.
     * @return {@link ResponseEntity<ProductDto>} product details that were added.
     */
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ProductDto> addProduct(@RequestBody ProductDto productDto) {
        return new ResponseEntity<>(productService.addProduct(productDto), HttpStatus.CREATED);
    }

    /**
     * <p>
     *     Adds a new review to a product.
     * </p>
     * @param productId The product id to which the review is to be added.
     * @param reviewDto The review details to be added.
     * @return {@link ResponseEntity<ReviewDto>} review details that were added.
     */
    @PostMapping("/{productId}/reviews")
    public ResponseEntity<ReviewDto> addProductReview(@RequestAttribute("id") String userId,
                                                      @PathVariable String productId,
                                                      @RequestBody ReviewDto reviewDto) {
        return new ResponseEntity<>(reviewService.addReview(userId, reviewDto, productId), HttpStatus.CREATED);
    }

    /**
     * <p>
     *     Returns all the products in the database.
     * </p>
     * @param page The page number.
     * @param size The number of products to be returned.
     * @return {@link ResponseEntity<Set<ProductDto>} set of products.
     */
    @GetMapping
    public ResponseEntity<Set<ProductDto>> getProducts(@RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "10") int size) {
        return new ResponseEntity<>(productService.getAllProducts(page, size), HttpStatus.OK);
    }

    /**
     * <p>
     *     Returns the product details of a product.
     * </p>
     * @param productId The product id.
     * @return {@link ResponseEntity<Product>} product details.
     */
    @GetMapping("/{productId}")
    public ResponseEntity<Product> getProduct(@PathVariable String productId) {
        return new ResponseEntity<>(productService.getProductById(productId), HttpStatus.OK);
    }

    /**
     * <p>
     *     Returns the reviews of a product.
     * </p>
     * @param page The page number.
     * @param size The number of reviews to be returned.
     * @param productId The product id.
     * @return {@link ResponseEntity<Set<ReviewDto>} set of reviews.
     */
    @GetMapping("/{productId}/reviews")
    public ResponseEntity<Set<ReviewDto>> getProductReviews(@RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "10") int size,
                                                            @PathVariable String productId) {
        return new ResponseEntity<>(reviewService.getReviewsByProductId(productId, page, size), HttpStatus.OK);
    }

    /**
     * <p>
     *     Updates the product details.
     * </p>
     * @param productDto The product details to be updated.
     * @return {@link ResponseEntity<ProductDto>} updated product details.
     */
    @PatchMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductDto> updateProduct(@RequestBody ProductDto productDto) {
        return new ResponseEntity<>(productService.updateProduct(productDto), HttpStatus.OK);
    }

    /**
     * <p>
     *     Deletes a product from the database.
     * </p>
     * @param productId The product id to be deleted.
     * @return {@link ResponseEntity<HttpStatus>} status of the request.
     */
    @DeleteMapping("/{productId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<HttpStatus> deleteProduct(@PathVariable String productId) {
        productService.deleteProduct(productId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}