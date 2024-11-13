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
            logger.info("Order item saved successfully");
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
     * @param cart      the cart to add the item to
     * @param orderItem the item to add
     */
    public void addToCart(Cart cart, OrderItemDto orderItem) {
        Product product = productService.getProductModelById(orderItem.getProduct().getId());
        OrderItem item = OrderItemMapper.dtoToModel(orderItem);
        item.setProduct(product);
        item.setCart(cart);
        item.setPrice(product.getPrice() * item.getQuantity());
        item.setAudit("SYSTEM");
        saveOrderItem(item);
    }

    /**
     * <p>
     * Updates the cart items.
     * </p>
     *
     * @param orderItems    the order items to update
     * @param orderItemDtos the order item dtos to update
     * @param cart          the cart to update
     * @return {@link Set<OrderItem>}the updated order items
     */
    public Set<OrderItem> updateCartItems(Set<OrderItem> orderItems, Set<OrderItemDto> orderItemDtos, Cart cart) {
        OrderItemDto orderItemDto = orderItemDtos.stream().findFirst().get();
        Product product = productService.getProductModelById(orderItemDto.getProduct().getId());
        for (OrderItem orderItem : orderItems) {
            if (orderItem.getProduct().getId().equals(product.getId())) {
                if (product.getQuantity() + orderItem.getQuantity() < orderItemDto.getQuantity()) {
                    throw new NoSuchElementException("Product quantity is less than the requested quantity");
                }
                product.setQuantity(product.getQuantity() + orderItem.getQuantity() - orderItemDto.getQuantity());
                productService.saveProduct(product);
                cart.setPrice(cart.getPrice() - orderItem.getPrice());
                cart.setQuantity(cart.getQuantity() - orderItem.getQuantity());
                orderItem.setQuantity(orderItemDto.getQuantity());
                orderItem.setPrice(product.getPrice() * orderItemDto.getQuantity());
                cart.setPrice(cart.getPrice() + orderItem.getPrice());
                cart.setQuantity(cart.getQuantity() + orderItem.getQuantity());
                return orderItems;
            }
        }
        if (product.getQuantity() < orderItemDto.getQuantity()) {
            throw new NoSuchElementException("Product quantity is less than the requested quantity");
        }
        product.setQuantity(product.getQuantity() - orderItemDto.getQuantity());
        productService.saveProduct(product);
        OrderItem orderItem = OrderItem.builder()
                .id(UUID.randomUUID().toString())
                .product(product)
                .quantity(orderItemDto.getQuantity())
                .price(product.getPrice() * orderItemDto.getQuantity())
                .cart(cart)
                .build();
        orderItem.setAudit(cart.getUser().getId());
        cart.setPrice(cart.getPrice() + orderItem.getPrice());
        cart.setQuantity(cart.getQuantity() + orderItem.getQuantity());
        orderItems.add(orderItem);
        return orderItems;
    }

    public void releaseProducts(Set<OrderItem> orderItems) {
        for (OrderItem orderItem : orderItems) {
            Product product = orderItem.getProduct();
            product.setQuantity(product.getQuantity() + orderItem.getQuantity());
            productService.saveProduct(product);
        }
    }
}
