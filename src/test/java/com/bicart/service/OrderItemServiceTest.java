package com.bicart.service;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.bicart.dto.OrderItemDto;
import com.bicart.dto.OrderProductDto;
import com.bicart.helper.BiCartException;
import com.bicart.model.Cart;
import com.bicart.model.OrderItem;
import com.bicart.model.Product;
import com.bicart.repository.OrderItemRepository;

@ExtendWith(MockitoExtension.class)
class OrderItemServiceTest {

    @InjectMocks
    private OrderItemService orderItemService;

    @Mock
    private OrderItemRepository orderItemRepository;
    @Mock
    private ProductService productService;

    private OrderItem orderItem;
    private Set<OrderItem> orderItems;
    private Set<OrderItemDto> orderItemDtos;
    private OrderItemDto orderItemDto;
    private Product product;
    private OrderProductDto orderProductDto;
    private Cart cart;

    @BeforeEach
    void setUp() {
        product = Product.builder()
                .id("1")
                .name("TestName")
                .quantity(3)
                .build();
        orderProductDto = OrderProductDto.builder()
                .id("1")
                .name("testName")
                .build();
        orderItems = Set.of(OrderItem.builder()
                .id("1")
                .price(100)
                .product(product)
                .build());
        orderItemDtos = Set.of(OrderItemDto.builder()
                .id("1")
                .price(100)
                .product(orderProductDto)
                .build());
        orderItem = OrderItem.builder()
                .id("1")
                .quantity(3)
                .price(100)
                .product(product)
                .build();
        orderItemDto = OrderItemDto.builder()
                .id("1")
                .quantity(3)
                .price(100)
                .product(orderProductDto)
                .build();
        cart = Cart.builder()
                .id("1")
                .price(200)
                .build();
    }


    @Test
    void testReleaseProductsSuccess() {
        when(productService.saveProduct(product)).thenReturn(product);
        orderItemService.releaseProducts(orderItems);
    }

    @Test
    void testSaveOrderItemSuccess() {
        when(orderItemRepository.save(orderItem)).thenReturn(null);
        orderItemService.saveOrderItem(orderItem);
        verify(orderItemRepository, times(1)).save(any(OrderItem.class));
    }

    @Test
    void testSaveOrderItemThrowsCustomException() {
        when(orderItemRepository.save(orderItem)).thenThrow(BiCartException.class);
        assertThrows(BiCartException.class, () -> orderItemService.saveOrderItem(orderItem));
    }

    @Test
    void testUpdateCartItemsSuccess() {
        Product product2 = Product.builder()
                .id("2")
                .quantity(10)
                .build();
        orderItem.setProduct(product2);
        when(productService.getProductModelById(anyString())).thenReturn(product);
        when(productService.saveProduct(product)).thenReturn(null);
        orderItemService.updateCartItems(orderItems, orderItemDtos, cart);
    }
}