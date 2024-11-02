package com.bicart.service;

import com.bicart.dto.CartDto;
import com.bicart.dto.OrderItemDto;
import com.bicart.helper.CustomException;
import com.bicart.mapper.CartMapper;
import com.bicart.model.Cart;
import com.bicart.model.OrderItem;
import com.bicart.repository.CartRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    private final static Logger logger = LogManager.getLogger(CartService.class);
    @Autowired
    private OrderItemService orderItemService;

    public CartDto getCartByUserId(String userId) {
        try {
            Cart cart = cartRepository.findByUserId(userId);
            List<OrderItemDto> items = orderItemService.getOrderItemsByCartId(cart.getId());
            return CartMapper.modelToDto(cart);
        } catch (Exception e) {
            logger.error("Error getting cart by user id", e);
            throw new CustomException("Error getting cart by user id " + userId);
        }
    }

    public CartDto saveCart(CartDto cartDto) {
        try {
            Cart cart = CartMapper.dtoToModel(cartDto);
            cart = cartRepository.save(cart);
            return CartMapper.modelToDto(cart);
        } catch (Exception e) {
            logger.error("Error saving cart", e);
            throw new CustomException("Error saving cart");
        }
    }

}
