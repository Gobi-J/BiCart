package com.bicart.controller;

import com.bicart.dto.CartDto;
import com.bicart.model.Cart;
import com.bicart.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/users/me/carts")
@RequiredArgsConstructor
public class UserCartController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<Cart> getCart(@RequestAttribute("id") String userId) {
        return new ResponseEntity<>(cartService.getCart(userId), HttpStatus.OK);
    }

    @PatchMapping
    public ResponseEntity<CartDto> addToCart(@RequestAttribute("id") String userId, CartDto cartDto) {
        return new ResponseEntity<>(cartService.addToCart(userId, cartDto), HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<HttpStatus> deleteCart(@RequestAttribute("id") String userId) {
        cartService.deleteCart(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
