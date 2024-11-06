package com.bicart.service;

import com.bicart.dto.CartDto;
import com.bicart.dto.OrderItemDto;
import com.bicart.helper.CustomException;
import com.bicart.mapper.CartMapper;
import com.bicart.model.Cart;
import com.bicart.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final OrderItemService orderItemService;

    private final static Logger logger = LogManager.getLogger(CartService.class);


    public CartDto getCartByUserId(String userId) {
        try {
            Cart cart = cartRepository.findByUserId(userId);
            Set<OrderItemDto> items = orderItemService.getOrderItemsByCartId(cart.getId());
            logger.info("Retrieved Cart for the user id: {}", userId);
            return CartMapper.modelToDto(cart);
        } catch (Exception e) {
            logger.error("Error in getting cart by user id: {}", userId, e);
            throw new CustomException("Error getting cart by user id " + userId, e);
        }
    }

    public CartDto saveCart(CartDto cartDto) {
        try {
            Cart cart = CartMapper.dtoToModel(cartDto);
            cart = cartRepository.save(cart);
            logger.info("Cart saved successfully with the id: {}", cart.getId());
            return CartMapper.modelToDto(cart);
        } catch (Exception e) {
            logger.error("Error saving cart with the id: {} ", cartDto.getId(), e);
            throw new CustomException("Error saving cart", e);
        }
    }
}