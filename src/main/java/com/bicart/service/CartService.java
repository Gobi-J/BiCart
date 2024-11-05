package com.bicart.service;

import com.bicart.dto.CartDto;
import com.bicart.dto.OrderItemDto;
import com.bicart.helper.CustomException;
import com.bicart.mapper.CartMapper;
import com.bicart.model.Cart;
import com.bicart.model.OrderItem;
import com.bicart.model.Product;
import com.bicart.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final OrderItemService orderItemService;
    private final UserService userService;

    private final static Logger logger = LogManager.getLogger(CartService.class);


    public Cart getCart(String userId) {
        try {
            return cartRepository.findByUserId(userId);
        } catch (Exception e) {
            logger.error("Error in getting cart by user id: {}", userId, e);
            throw new CustomException("Error getting cart by user id " + userId, e);
        }
    }

    public Cart saveCart(Cart cart) {
        try {
            return cartRepository.save(cart);
        } catch (Exception e) {
            logger.error("Error saving cart with the id: {} ", cart.getId(), e);
            throw new CustomException("Error saving cart", e);
        }
    }

    public CartDto addToCart(String userId, CartDto cartDto) {
        try {
            Cart cart = getCart(userId);
            if (cart == null) {
                cart = new Cart();
                cart.setUser(userService.getUserModelById(cartDto.getUser().getId()));
            }
            Set<OrderItem> orderItems = cart.getOrderItems();
            if (orderItems == null) {
                orderItems = new HashSet<>();
            }
            cart.setOrderItems(orderItemService.updateCartItems(orderItems, cartDto.getOrderItems(), cart));
            cart = saveCart(cart);
            return CartMapper.modelToDto(cart);
        } catch (Exception e) {
            logger.error("Error adding to cart", e);
            throw new CustomException("Error adding to cart");
        }
    }

    public void deleteCart(String userId) {
        try {
            Cart cart = getCart(userId);
            cartRepository.delete(cart);
        } catch (Exception e) {
            logger.error("Error deleting cart", e);
            throw new CustomException("Error deleting cart");
        }
    }
}