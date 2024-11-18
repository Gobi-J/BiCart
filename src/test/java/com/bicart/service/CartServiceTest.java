package com.bicart.service;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.bicart.dto.CartDto;
import com.bicart.dto.OrderItemDto;
import com.bicart.helper.BiCartException;
import com.bicart.model.Cart;
import com.bicart.model.OrderItem;
import com.bicart.model.User;
import com.bicart.repository.CartRepository;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @InjectMocks
    private CartService cartService;

    @Mock
    private CartRepository cartRepository;
    @Mock
    private UserService userService;
    @Mock
    private OrderItemService orderItemService;

    private Cart cart;
    private CartDto cartDto;
    private User user;
    private Set<OrderItem> orderItems;
    private Set<OrderItemDto> orderItemsDto;

    @BeforeEach
    void setUp() {


        user = User.builder()
                .id("1")
                .name("TestName")
                .mobileNumber("9234567890")
                .build();

        orderItems = Set.of(OrderItem.builder()
                .id("1")
                .price(100)
                .quantity(3)
                .build());
        orderItemsDto = Set.of(OrderItemDto.builder()
                .id("1")
                .price(100)
                .quantity(3)
                .build());
        cart = Cart.builder()
                .id("1")
                .quantity(3)
                .price(100)
                .orderItems(orderItems)
                .build();
        cartDto = CartDto.builder()
                .id("1")
                .quantity(3)
                .price(100)
                .orderItems(orderItemsDto)
                .build();
    }

    @Test
    void testGetCartSuccess() {
        when(cartRepository.findByUserId("1")).thenReturn(cart);
        Cart result = cartService.getCart("1");
        assertNotNull(result);
        assertEquals(cartDto.getQuantity(), result.getQuantity());
    }

    @Test
    void testGetCartThrowsException() {
        when(cartRepository.findByUserId("1")).thenThrow(BiCartException.class);
        assertThrows(BiCartException.class, () -> cartService.getCart("1"));
    }

    @Test
    void testAddToCartSuccess() {
        when(userService.getUserModelById(anyString())).thenReturn(user);
        when(orderItemService.updateCartItems(any(Set.class), any(Set.class), any(Cart.class))).thenReturn(orderItems);
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);
        cartService.addToCart("1", cartDto);
        verify(cartRepository, times(1)).save(any(Cart.class));
    }


    @Test
    void testGetCartByUserIdSuccess() {
        when(cartRepository.findByUserId(anyString())).thenReturn(cart);
        Cart result = cartService.getCart(anyString());
        assertNotNull(result);
        assertEquals(result.getPrice(), cart.getPrice());
    }

}