package com.example.midterm.controller;

import com.example.midterm.model.Cart;
import com.example.midterm.model.CartItem;
import com.example.midterm.model.Product;
import com.example.midterm.model.User;
import com.example.midterm.repository.UserRepository;
import com.example.midterm.service.ProductService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @MockBean
    private Model model;
    @MockBean
    private Authentication authentication;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private List<CartItem> cartItems;

    @Autowired
    private ProductController productController;

    @MockBean
    private User user;

    @MockBean
    private Cart cart;



    @Test
    public void testSearchProducts() {
        // Mocking
        String keyword = "phone";
        Double minPrice = 100.0;
        Double maxPrice = 500.0;

        List<Product> products = new ArrayList<>();
        // Add mocked products to the list

        String username = "testUser";
        // Add mocked cart items to the cart

        when(authentication.getName()).thenReturn(username);
        when(userRepository.findByUsername(username)).thenReturn(user);
        when(user.getCart()).thenReturn(cart);
        when(cart.getItems()).thenReturn(cartItems);
        // Mock productRepository.search(...) to return the products list

        // Testing
        String viewName = productController.searchProducts(keyword, minPrice, maxPrice, model, authentication);

        // Verification
        assertEquals("index-filter", viewName);
        Mockito.verify(model, times(1)).addAttribute("total", cart.getTotal());
        Mockito.verify(model, times(1)).addAttribute("cartItems", cartItems);
        Mockito.verify(model, times(1)).addAttribute("username", username);
        Mockito.verify(model, times(1)).addAttribute("productFilters", products);
    }

    @Test
    public void testGetProductById() {
        // Mocking
        Long id = 1L;
        String username = "testUser";

        Product product = new Product(); // Create a mocked product

        when(authentication.getName()).thenReturn(username);
        when(productService.getProductById(id)).thenReturn(product);

        // Testing
        String viewName = productController.getProductById(id, model, authentication);

        // Verification
        assertEquals("product-detail", viewName);
        Mockito.verify(model, times(1)).addAttribute("productDetail", product);
        Mockito.verify(model, times(1)).addAttribute("username", username);
    }


    @Test
    public void testAddToCart() throws Exception {
        mockMvc.perform(get("/products/{id}/addToCart", 1L))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    public void testAddProduct() throws Exception {
        mockMvc.perform(post("/addProduct")
                        .param("name", "test")
                        .param("category", "test")
                        .param("price", "1.0")
                        .param("brand", "test")
                        .param("color", "test")
                        .param("image", "test"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    public void testDeleteProduct() throws Exception {
        mockMvc.perform(get("/products/{id}/delete", 1L))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }
}
