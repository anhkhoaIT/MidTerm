package com.example.midterm.controller;

import com.example.midterm.model.Cart;
import com.example.midterm.model.CartItem;
import com.example.midterm.model.Order;
import com.example.midterm.model.User;
import com.example.midterm.repository.CartRepository;
import com.example.midterm.repository.OrderRepository;
import com.example.midterm.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@SpringBootTest
@AutoConfigureMockMvc
public class CartControllerTest {
    @Autowired
    private MockMvc mockMvc;


    @Autowired
    private CartController cartController;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private CartRepository cartRepository;

    @MockBean
    private OrderRepository orderRepository;

    @Test
    public void testCheckoutSuccess() {
        // Arrange
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("testUser");

        User user = new User();
        Cart cart = new Cart();
        List<CartItem> cartItems = new ArrayList<>(Arrays.asList(new CartItem(), new CartItem()));
        cart.setItems(cartItems);
        user.setCart(cart);

        when(userRepository.findByUsername("testUser")).thenReturn(user);

        // Act
        String result = cartController.checkoutSuccess(authentication);

        // Assert
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(cartRepository, times(1)).save(any(Cart.class));
        assertEquals("redirect:/order", result);
    }
}
