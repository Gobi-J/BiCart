package com.bicart.controller;

import java.util.Map;
import java.util.Set;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
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
import com.bicart.helper.SuccessResponse;
import com.bicart.service.ProductService;
import com.bicart.service.ReviewService;

/**
 * <p>
 * ProductController class is a REST controller that handles all the requests related to products.
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
     * Adds a new product to the database.
     * </p>
     *
     * @param product  details to be added.
     * @return {@link SuccessResponse} with {@link HttpStatus} CREATED.
     */
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<SuccessResponse> addProduct(@Validated @RequestBody ProductDto product) {
        productService.addProduct(product);
        return SuccessResponse.setSuccessResponse("Product Added Successfully", HttpStatus.CREATED);
    }

    /**
     * <p>
     * Adds a new review to a product.
     * </p>
     *
     * @param userId    who is adding the review.
     * @param productId The product id to which the review is to be added.
     * @param reviewDto The review details to be added.
     * @return {@link SuccessResponse} with {@link HttpStatus} CREATED.
     */
    @PostMapping("/{productId}/reviews")
    public ResponseEntity<SuccessResponse> addProductReview(@RequestAttribute("id") String userId,
                                                            @PathVariable String productId,
                                                            @RequestBody ReviewDto reviewDto) {
        reviewService.addReview(userId, reviewDto, productId);
        return SuccessResponse.setSuccessResponse("Review added Successfully", HttpStatus.CREATED);
    }

    /**
     * <p>
     * Returns all the products in the database.
     * </p>
     *
     * @param page The page number.
     * @param size The number of products to be returned.
     * @return {@link SuccessResponse} containing the list of products, with {@link HttpStatus} OK.
     */
    @GetMapping
    public ResponseEntity<SuccessResponse> getProducts(@RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "10") int size) {
        Set<ProductDto> products = productService.getAllProducts(page, size);
        return SuccessResponse.setSuccessResponse("Products fetched Successfully", HttpStatus.OK, Map.of("products", products));
    }

    /**
     * <p>
     * Returns the product details of a product.
     * </p>
     *
     * @param productId The product id.
     * @return {@link SuccessResponse} containing the product details, with {@link HttpStatus} OK.
     */
    @GetMapping("/{productId}")
    public ResponseEntity<SuccessResponse> getProduct(@PathVariable String productId) {
        ProductDto product = productService.getProductById(productId);
        return SuccessResponse.setSuccessResponse("Product fetched Successfully", HttpStatus.OK, Map.of("product", product));
    }

    /**
     * <p>
     * Returns the reviews of a product.
     * </p>
     *
     * @param page      page number.
     * @param size      number of reviews to be returned.
     * @param productId which reviews are to be fetched.
     * @return {@link SuccessResponse} containing the reviews of the product, with {@link HttpStatus} OK.
     */
    @GetMapping("/{productId}/reviews")
    public ResponseEntity<SuccessResponse> getProductReviews(@RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "10") int size,
                                                             @PathVariable String productId) {
        Set<ReviewDto> reviews = reviewService.getReviewsByProductId(productId, page, size);
        return SuccessResponse.setSuccessResponse("Reviews of the product fetched Successfully", HttpStatus.OK, Map.of("reviews", reviews));
    }

    /**
     * <p>
     * Updates the product details.
     * </p>
     *
     * @param productDto The product details to be updated.
     * @return {@link SuccessResponse} containing the updated product details, with {@link HttpStatus} OK.
     */
    @PatchMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SuccessResponse> updateProduct(@RequestBody ProductDto productDto) {
        ProductDto product = productService.updateProduct(productDto);
        return SuccessResponse.setSuccessResponse("Product updated Successfully", HttpStatus.OK, Map.of("product", product));
    }

    /**
     * <p>
     * Deletes a product from the database.
     * </p>
     *
     * @param productId The product id to be deleted.
     * @return {@link SuccessResponse} with {@link HttpStatus} NO_CONTENT.
     */
    @DeleteMapping("/{productId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<SuccessResponse> deleteProduct(@PathVariable String productId) {
        productService.deleteProduct(productId);
        return SuccessResponse.setSuccessResponse("Product deleted Successfully", HttpStatus.NO_CONTENT);
    }
}