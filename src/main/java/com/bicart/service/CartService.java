package com.bicart.service;

import java.util.HashSet;
import java.util.Set;

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
     * Fetches a cart by user id.
     * </p>
     *
     * @param userId the id of the user to fetch the cart for
     * @return {@link Cart} cart of the user
     * @throws CustomException if an error occurs while fetching the cart
     */
    protected Cart getCart(String userId) {
        try {
            return cartRepository.findByUserId(userId);
        } catch (Exception e) {
            logger.error("Error in getting cart by user id: {}", userId, e);
            throw new CustomException("Error getting cart by user id " + userId);
        }
    }

    /**
     * <p>
     * Saves a cart.
     * </p>
     *
     * @param cart the cart to save
     * @throws CustomException if an error occurs while saving the cart
     */
    protected void saveCart(Cart cart) {
        try {
            cartRepository.save(cart);
        } catch (Exception e) {
            logger.error("Error saving cart with the id: {} ", cart.getId(), e);
            throw new CustomException("Cannot save cart. Try again");
        }
    }

    /**
     * <p>
     * Adds an item to the cart.
     * </p>
     *
     * @param userId  the id of the user to add the item to the cart for
     * @param cartDto containing details of the item to add
     */
    public void addToCart(String userId, CartDto cartDto) {
        Cart cart = getCart(userId);
        if (cart == null) {
            cart = new Cart();
            cart.setUser(userService.getUserModelById(userId));
            cart.setAudit(userId);
        }
        Set<OrderItem> orderItems = cart.getOrderItems();
        if (orderItems == null) {
            orderItems = new HashSet<>();
        }
        orderItems = orderItemService.updateCartItems(orderItems, cartDto.getOrderItems(), cart);
        cart.setOrderItems(orderItems);
        saveCart(cart);
    }

    /**
     * <p>
     * Deletes a cart.
     * </p>
     *
     * @param userId the id of the user to delete the cart
     */
    public void deleteCart(String userId) {
        Cart cart = getCart(userId);
        for(OrderItem orderItem: cart.getOrderItems()) {
            orderItem.setCart(null);
            orderItemService.saveOrderItem(orderItem);
        }
        cartRepository.delete(cart);
        logger.info("Cart deleted successfully for user id: {}", userId);
    }

    /**
     * <p>
     * Fetches a cart by user id.
     * </p>
     *
     * @param userId the id of the user to fetch the cart for
     * @return {@link CartDto} cart of the user
     */
    public CartDto getCartByUserId(String userId) {
        Cart cart = getCart(userId);
        if (cart == null) {
            return null;
        }
        return CartMapper.modelToDto(cart);
    }

    /**
     * <p>
     * Deletes a cart before order.
     * </p>
     *
     * @param userId the id of the user to delete the cart before order
     */
    public void deleteCartBeforeOrder(String userId) {
        Cart cart = getCart(userId);
        orderItemService.releaseProducts(cart.getOrderItems());
        cart.getOrderItems().forEach(orderItem -> orderItem.setCart(null));
        cartRepository.delete(cart);
    }
}