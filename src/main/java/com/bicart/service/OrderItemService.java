package com.bicart.service;

import com.bicart.dto.OrderItemDto;
import com.bicart.mapper.OrderItemMapper;
import com.bicart.model.Cart;
import com.bicart.model.OrderItem;
import com.bicart.model.Product;
import com.bicart.repository.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final ProductService productService;

    private final static Logger logger = LogManager.getLogger(OrderItemService.class);

    public Set<OrderItemDto> getOrderItemsByOrderId(String orderId) {
        try {
            return orderItemRepository.findByOrderId(orderId);
        } catch (Exception e) {
            logger.error("Error getting order items by order id", e);
        }
        return null;
    }

    public Set<OrderItemDto> getOrderItemsByCartId(String cartId) {
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
            Set<OrderItemDto> orderItems = getOrderItemsByCartId(cartId);
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

    public Set<OrderItem> updateCartItems(Set<OrderItem> orderItems, Set<OrderItemDto> orderItemDtos, Cart cart) {
        OrderItemDto orderItemDto = orderItemDtos.stream().findFirst().get();
        Product product = productService.getProductById(orderItemDto.getProduct().getId());
        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(product);
        orderItem.setQuantity(orderItemDto.getQuantity());
        orderItem.setPrice(product.getPrice() * orderItemDto.getQuantity());
        orderItem.setCart(cart);
        cart.setPrice(cart.getPrice() + orderItem.getPrice());
        cart.setQuantity(cart.getQuantity() + orderItem.getQuantity());
        orderItems.add(orderItem);
        return orderItems;
    }
}
