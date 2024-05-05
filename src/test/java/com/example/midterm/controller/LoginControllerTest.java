package com.example.midterm.controller;

import com.example.midterm.model.Cart;
import com.example.midterm.model.CartItem;
import com.example.midterm.model.User;
import com.example.midterm.repository.UserRepository;
import com.example.midterm.service.ProductService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class LoginControllerTest {
    @MockBean
    private Authentication authentication;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private ProductService productService;

    @Autowired
    private LoginController loginController;
    @MockBean
    private Model model;

    @Test
    public void testHome() {
        String username = "testUser";
        User user = new User();
        Cart cart = new Cart();
        List<CartItem> cartItems = new ArrayList<>();
        cart.setItems(cartItems);
        user.setCart(cart);

        when(authentication.getName()).thenReturn(username);
        when(userRepository.findByUsername(username)).thenReturn(user);
        when(productService.getAllProducts()).thenReturn(new ArrayList<>());

        String viewName = loginController.home(authentication, model);

        verify(model).addAttribute("total", cart.getTotal());
        verify(model).addAttribute("cartItems", cartItems);
        verify(model).addAttribute("username", username);
        verify(model).addAttribute("products", productService.getAllProducts());

        assertEquals("index", viewName);
    }
}
