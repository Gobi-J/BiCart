package com.bicart.service;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.bicart.dto.CartDto;
import com.bicart.helper.CustomException;
import com.bicart.mapper.CartMapper;
import com.bicart.model.Cart;
import com.bicart.model.OrderItem;
import com.bicart.repository.CartRepository;

/**
 * <p>
 * Service class that handles business logic related to cart
 * </p>
 */
@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final OrderItemService orderItemService;
    private final UserService userService;

    private final static Logger logger = LogManager.getLogger(CartService.class);


    /**
     * <p>
     *   Fetches a cart by user id.
     * </p>
     *
     * @param userId the id of the user to fetch the cart for
     * @return {@link Cart} cart of the user
     * @throws CustomException if an error occurs while fetching the cart
     */
    public Cart getCart(String userId) {
        try {
            return cartRepository.findByUserId(userId);
        } catch (Exception e) {
            logger.error("Error in getting cart by user id: {}", userId, e);
            throw new CustomException("Error getting cart by user id " + userId, e);
        }
    }

    /**
     * <p>
     *   Saves a cart.
     * </p>
     *
     * @param cart the cart to save
     * @return {@link Cart} the saved cart
     * @throws CustomException if an error occurs while saving the cart
     */
    public Cart saveCart(Cart cart) {
        try {
            return cartRepository.save(cart);
        } catch (Exception e) {
            logger.error("Error saving cart with the id: {} ", cart.getId(), e);
            throw new CustomException("Error saving cart", e);
        }
    }

    /**
     * <p>
     * Adds an item to the cart.
     * </p>
     *
     * @param userId  the id of the user to add the item to the cart for
     * @param cartDto the cart dto containing the item to add
     * @throws CustomException if an error occurs while adding to the cart
     */
    public void addToCart(String userId, CartDto cartDto) {
            Cart cart = getCart(userId);
            if (cart == null) {
                cart = new Cart();
                cart.setId(UUID.randomUUID().toString());
                cart.setUser(userService.getUserModelById(userId));
                cart.setAudit(userId);
            }
            Set<OrderItem> orderItems = cart.getOrderItems();
            if (orderItems == null) {
                orderItems = new HashSet<>();
            }
            orderItems = orderItemService.updateCartItems(orderItems, cartDto.getOrderItems(), cart);
            cart.setOrderItems(orderItems);
        CartMapper.modelToDto(saveCart(cart));
    }

    /**
     * <p>
     *   Deletes a cart.
     * </p>
     *
     * @param userId the id of the user to delete the cart for
     * @throws CustomException if an error occurs while deleting the cart
     */
    public void deleteCart(String userId) {
            Cart cart = getCart(userId);
            cart.getOrderItems().forEach(orderItem -> orderItem.setCart(null));
            cartRepository.delete(cart);
            logger.info("Cart deleted successfully for user id: {}", userId);
    }

    public CartDto getCartByUserId(String userId) {
        Cart cart = getCart(userId);
        return CartMapper.modelToDto(getCart(userId));
    }
}