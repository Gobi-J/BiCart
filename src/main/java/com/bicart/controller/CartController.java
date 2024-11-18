package com.bicart.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bicart.dto.CartDto;
import com.bicart.helper.SuccessResponse;
import com.bicart.service.CartService;

/**
 * <p>
 * CartController class is a REST controller class that handles all the requests related to cart.
 * </p>
 */
@RestController
@RequestMapping("/v1/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    /**
     * <p>
     * Get the cart of the user.
     * </p>
     *
     * @param userId fetched from the request attribute
     * @return {@link SuccessResponse} containing the cart of the user, with {@link HttpStatus} OK
     */
    @GetMapping
    public ResponseEntity<SuccessResponse> getCart(@RequestAttribute("id") String userId) {
        CartDto cartDto = cartService.getCartByUserId(userId);
        String message = "Cart fetched successfully";
        if (ObjectUtils.isEmpty(cartDto)) {
            message = "Cart is empty";
        }
        return SuccessResponse.setSuccessResponseOk(message, cartDto);
    }

    /**
     * <p>
     * Add a product to the cart of the user.
     * </p>
     *
     * @param userId  fetched from the request attribute
     * @param cartDto containing the product to be added to the cart
     * @return {@link SuccessResponse} with {@link HttpStatus} OK
     */
    @PutMapping
    public ResponseEntity<SuccessResponse> addToCart(@Validated @RequestAttribute("id") String userId, @RequestBody CartDto cartDto) {
        cartService.addToCart(userId, cartDto);
        return SuccessResponse.setSuccessResponseOk("Cart updated successfully", null);
    }

    /**
     * <p>
     * Delete the cart of the user.
     * </p>
     *
     * @param userId fetched from the request attribute
     * @return {@link SuccessResponse} with {@link HttpStatus} NO_CONTENT
     */
    @DeleteMapping
    public ResponseEntity<SuccessResponse> deleteCart(@RequestAttribute("id") String userId) {
        cartService.deleteCartBeforeOrder(userId);
        return SuccessResponse.setSuccessResponseNoContent("Cart deleted successfully");
    }
}