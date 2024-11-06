package com.bicart.controller;

import com.bicart.dto.CartDto;
import com.bicart.model.Cart;
import com.bicart.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *     CartController class is a REST controller class that handles all the requests related to cart.
 * </p>
 */
@RestController
@RequestMapping("/v1/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    /**
     * <p>
     *     Get the cart of the user.
     * </p>
     *
     * @param userId fetched from the request attribute
     * @return {@link ResponseEntity<Cart>} containing the cart of the user, with HTTP status OK
     */
    @GetMapping
    public ResponseEntity<Cart> getCart(@RequestAttribute("id") String userId) {
        return new ResponseEntity<>(cartService.getCart(userId), HttpStatus.OK);
    }

    /**
     * <p>
     *     Add a product to the cart of the user.
     * </p>
     *
     * @param userId fetched from the request attribute
     * @param cartDto containing the product to be added to the cart
     * @return {@link ResponseEntity<CartDto>} containing the updated cart of the user, with HTTP status OK
     */
    @PutMapping
    public ResponseEntity<CartDto> addToCart(@RequestAttribute("id") String userId, CartDto cartDto) {
        return new ResponseEntity<>(cartService.addToCart(userId, cartDto), HttpStatus.OK);
    }

    /**
     * <p>
     *     Delete the cart of the user.
     * </p>
     *
     * @param userId fetched from the request attribute
     * @return {@link ResponseEntity<HttpStatus>} with {@link HttpStatus} NO_CONTENT
     */
    @DeleteMapping
    public ResponseEntity<HttpStatus> deleteCart(@RequestAttribute("id") String userId) {
        cartService.deleteCart(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}