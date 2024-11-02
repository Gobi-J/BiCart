package com.bicart.service;

import com.bicart.dto.OrderItemDto;
import com.bicart.mapper.OrderItemMapper;
import com.bicart.model.Cart;
import com.bicart.model.OrderItem;
import com.bicart.model.Product;
import com.bicart.repository.OrderItemRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderItemService {

    @Autowired
    private OrderItemRepository orderItemRepository;

    private final static Logger logger = LogManager.getLogger(OrderItemService.class);
    @Autowired
    private ProductService productService;

    public List<OrderItemDto> getOrderItemsByOrderId(String orderId) {
        try {
            return orderItemRepository.findByOrderId(orderId);
        } catch (Exception e) {
            logger.error("Error getting order items by order id", e);
        }
        return null;
    }

    public List<OrderItemDto> getOrderItemsByCartId(String cartId) {
        try {
            return orderItemRepository.findByCartId(cartId);
        } catch (Exception e) {
            logger.error("Error getting order items by order id", e);
        }
        return null;
    }

    public OrderItem saveOrderItem(OrderItem orderItem) {
        try {
            return orderItemRepository.save(orderItem);
        } catch (Exception e) {
            logger.error("Error saving order item", e);
        }
        return null;
    }

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
        }
    }

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
        }
    }

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
        }
    }
}
