package com.bicart.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.bicart.dto.OrderItemDto;
import com.bicart.helper.CustomException;
import com.bicart.mapper.OrderItemMapper;
import com.bicart.model.Cart;
import com.bicart.model.OrderItem;
import com.bicart.model.Product;
import com.bicart.repository.OrderItemRepository;

/**
 * <p>
 * Service class that handles business logic related to order items.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final ProductService productService;

    private final static Logger logger = LogManager.getLogger(OrderItemService.class);

    /**
     * <p>
     * Fetches order items by order id.
     * </p>
     *
     * @param orderId the id of the order to fetch order items for
     * @return {@link List<OrderItemDto>} of order items
     */
    public List<OrderItemDto> getOrderItemsByOrderId(String orderId) {
        try {
            return orderItemRepository.findByOrderId(orderId);
        } catch (Exception e) {
            logger.error("Error getting order items by order id", e);
            throw new CustomException("Error getting order items by order id");
        }
    }

    /**
     * <p>
     * Fetches order items by cart id.
     * </p>
     *
     * @param cartId the id of the cart to fetch order items for
     * @return {@link Set<OrderItemDto>} of order items
     */
    public List<OrderItemDto> getOrderItemsByCartId(String cartId) {
        try {
            return orderItemRepository.findByCartId(cartId);
        } catch (Exception e) {
            logger.error("Error getting order items by order id", e);
            throw new CustomException("Error getting order items by cart id");
        }
    }

    /**
     * <p>
     * Saves an order item.
     * </p>
     *
     * @param orderItem model.
     */
    public void saveOrderItem(OrderItem orderItem) {
        try {
            orderItemRepository.save(orderItem);
        } catch (Exception e) {
            logger.error("Error saving order item", e);
            throw new CustomException("Error saving order item");
        }
    }

    /**
     * <p>
     * Adds an item to the cart.
     * </p>
     *
     * @param cart the cart to add the item to
     * @param orderItem the item to add
     */
    public void addToCart(Cart cart, OrderItemDto orderItem) {
        try {
            Product product = productService.getProductById(orderItem.getProduct().getId());
            OrderItem item = OrderItemMapper.dtoToModel(orderItem);
            item.setProduct(product);
            item.setCart(cart);
            item.setPrice(product.getPrice() * item.getQuantity());
            saveOrderItem(item);
        } catch (Exception e) {
            logger.error("Error adding to cart", e);
            throw new CustomException("Error adding to cart");
        }
    }

    /**
     * <p>
     * Removes an item from the cart.
     * </p>
     *
     * @param cartId the id of the cart to remove the item from
     * @param productId the id of the product to remove
     */
    public void removeFromCart(String cartId, String productId) {
        try {
            List<OrderItemDto> orderItems = getOrderItemsByCartId(cartId);
            for (OrderItemDto orderItem : orderItems) {
                if (orderItem.getProduct().getId().equals(productId)) {
                    OrderItem item = orderItemRepository.findById(orderItem.getId()).get();
                    item.setCart(null);
                    saveOrderItem(item);
                    return;
                }
            }
        } catch (Exception e) {
            logger.error("Error removing from cart", e);
            throw new CustomException("Error removing from cart");
        }
    }

    /**
     * <p>
     * Updates an item in the cart.
     * </p>
     *
     * @param cart the cart to update the item in
     * @param orderItem the item to update
     */
    public void updateCart(Cart cart, OrderItemDto orderItem) {
        try {
            Product product = productService.getProductById(orderItem.getProduct().getId());
            OrderItem item = OrderItemMapper.dtoToModel(orderItem);
            item.setProduct(product);
            item.setCart(cart);
            item.setPrice(product.getPrice() * item.getQuantity());
            saveOrderItem(item);
        } catch (Exception e) {
            logger.error("Error updating cart", e);
            throw new CustomException("Error updating cart");
        }
    }

    /**
     * <p>
     * Updates the cart items.
     * </p>
     *
     * @param orderItems the order items to update
     * @param orderItemDtos the order item dtos to update
     * @param cart the cart to update
     * @return {@link Set<OrderItem>}the updated order items
     */
    public Set<OrderItem> updateCartItems(Set<OrderItem> orderItems, Set<OrderItemDto> orderItemDtos, Cart cart) {
        try {
            OrderItemDto orderItemDto = orderItemDtos.stream().findFirst().get();
            Product product = productService.getProductById(orderItemDto.getProduct().getId());
            for (OrderItem orderItem : orderItems) {
                if (orderItem.getProduct().getId().equals(product.getId())) {
                    orderItem.setQuantity(orderItemDto.getQuantity());
                    orderItem.setPrice(product.getPrice() * orderItemDto.getQuantity());
                    cart.setPrice(cart.getPrice() + orderItem.getPrice());
                    cart.setQuantity(cart.getQuantity() + orderItem.getQuantity());
                    return orderItems;
                }
            }
            OrderItem orderItem = new OrderItem();
            orderItem.setId(UUID.randomUUID().toString());
            orderItem.setProduct(product);
            orderItem.setQuantity(orderItemDto.getQuantity());
            orderItem.setPrice(product.getPrice() * orderItemDto.getQuantity());
            orderItem.setCart(cart);
            cart.setPrice(cart.getPrice() + orderItem.getPrice());
            cart.setQuantity(cart.getQuantity() + orderItem.getQuantity());
            orderItems.add(orderItem);
            return orderItems;
        } catch (Exception e) {
            if (e instanceof NoSuchElementException) {
                throw e;
            }
            logger.error("Error updating cart items", e);
            throw new CustomException("Error updating cart items");
        }
    }
}
