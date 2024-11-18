package com.bicart.service;

import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.bicart.dto.OrderItemDto;
import com.bicart.helper.BiCartException;
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
     * Saves an order item.
     * </p>
     *
     * @param orderItem details to save.
     */
    protected void saveOrderItem(OrderItem orderItem) {
        try {
            orderItemRepository.save(orderItem);
            logger.info("Order item saved successfully");
        } catch (Exception e) {
            logger.error("Error saving order item", e);
            throw new BiCartException("Error saving order item");
        }
    }

    /**
     * <p>
     *     Checks and updates the cart items.
     *     If the product is already in the cart, it updates the quantity.
     * </p>
     * @param orderItems the order items to update
     * @param product the product to update
     * @param orderItemDto the order item dto to update
     * @param cart the cart to update
     * @return {@link Boolean} true if the product is in the cart, false otherwise
     */
//    private boolean isProductInCart(Set<OrderItem> orderItems, Product product, OrderItemDto orderItemDto, Cart cart) {
//        for (OrderItem orderItem : orderItems) {
//            if (orderItem.getProduct().getId().equals(product.getId())) {
//                if (product.getQuantity() + orderItem.getQuantity() < orderItemDto.getQuantity()) {
//                    throw new NoSuchElementException("Product quantity is less than the requested quantity");
//                }
//                product.setQuantity(product.getQuantity() + orderItem.getQuantity() - orderItemDto.getQuantity());
//                productService.saveProduct(product);
//                cart.setPrice(cart.getPrice() - orderItem.getPrice());
//                cart.setQuantity(cart.getQuantity() - orderItem.getQuantity());
//                orderItem.setQuantity(orderItemDto.getQuantity());
//                orderItem.setPrice(product.getPrice() * orderItemDto.getQuantity());
//                cart.setPrice(cart.getPrice() + orderItem.getPrice());
//                cart.setQuantity(cart.getQuantity() + orderItem.getQuantity());
//                return true;
//            }
//        }
//        return false;
//    }

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
//    public Set<OrderItem> updateCartItems(Set<OrderItem> orderItems, Set<OrderItemDto> orderItemDtos, Cart cart) {
//        OrderItemDto orderItemDto = orderItemDtos.stream().findFirst().get();
//        Product product = productService.getProductModelById(orderItemDto.getProduct().getId());
//        if (isProductInCart(orderItems, product, orderItemDto, cart)) {
//            return orderItems;
//        }
//        if (product.getQuantity() < orderItemDto.getQuantity()) {
//            throw new NoSuchElementException("Product quantity is less than the requested quantity");
//        }
//        product.setQuantity(product.getQuantity() - orderItemDto.getQuantity());
//        productService.saveProduct(product);
//        OrderItem orderItem = OrderItem.builder()
//                .id(UUID.randomUUID().toString())
//                .product(product)
//                .quantity(orderItemDto.getQuantity())
//                .price(product.getPrice() * orderItemDto.getQuantity())
//                .cart(cart)
//                .build();
//        orderItem.setAudit(cart.getUser().getId());
//        cart.setPrice(cart.getPrice() + orderItem.getPrice());
//        cart.setQuantity(cart.getQuantity() + orderItem.getQuantity());
//        orderItems.add(orderItem);
//        return orderItems;
//    }
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

    /**
     * <p>
     * Removes an item from the cart.
     * </p>
     *
     * @param orderItems the order items to remove
     */
    public void releaseProducts(Set<OrderItem> orderItems) {
        for (OrderItem orderItem : orderItems) {
            Product product = orderItem.getProduct();
            product.setQuantity(product.getQuantity() + orderItem.getQuantity());
            productService.saveProduct(product);
            orderItemRepository.delete(orderItem);
        }
    }
}
