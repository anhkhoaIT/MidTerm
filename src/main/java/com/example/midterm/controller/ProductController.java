package com.example.midterm.controller;

import com.example.midterm.exception.ProductNotFoundException;
import com.example.midterm.model.Cart;
import com.example.midterm.model.CartItem;
import com.example.midterm.model.Product;
import com.example.midterm.model.User;
import com.example.midterm.repository.CartRepository;
import com.example.midterm.repository.ProductRepository;
import com.example.midterm.repository.UserRepository;
import com.example.midterm.service.CartService;
import com.example.midterm.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ProductController {
    @Autowired
    ProductService productService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CartRepository cartRepository;
    @Autowired
    CartService cartService;
    @Autowired
    ProductRepository productRepository;

    @GetMapping("/search")
    public String searchProducts(@RequestParam String keyword, @RequestParam(required = false) Double minPrice, @RequestParam(required = false) Double maxPrice, Model model, Authentication authentication) {
        List<Product> products = productRepository.search(keyword, minPrice, maxPrice);
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);
        Cart cart = user.getCart();
        List<CartItem> cartItems = cart.getItems();
        cart.calculateTotal();
        model.addAttribute("total", cart.getTotal());
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("username", username);
        model.addAttribute("productFilters", products);
        return "index-filter";

    }

    @GetMapping("/products/{id}")
    public String getProductById(@PathVariable Long id, Model model, Authentication authentication) {
        String username = authentication.getName();

        Product product = productService.getProductById(id);
        if(product == null) {
            throw new ProductNotFoundException("Product not found");
        }
        model.addAttribute("productDetail", product);
        model.addAttribute("username", username);
        return "product-detail";
    }

    @GetMapping("/products/{id}/addToCart")
    public String addToCart(@PathVariable Long id, Authentication authentication, Model model) {
        Product product = productService.getProductById(id);
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);
        if(product == null) {
            throw new ProductNotFoundException("Product not found");
        }
        Cart cart = user.getCart();
        if (cart == null) {
            cart = new Cart(user);
            user.setCart(cart);
        }
        cartService.incrementQuantity(cart, product);
        userRepository.save(user);
        model.addAttribute("successMessage", "Product added to cart successfully!");
        return "redirect:/";
    }

    @PostMapping("/addProduct")
    public String addProduct(@RequestParam String name, @RequestParam String category, @RequestParam double price, @RequestParam String brand, @RequestParam String color, @RequestParam String image) {
        Product product = new Product(name, category, price, brand, color, image);
        productRepository.save(product);
        return "redirect:/";
    }

    @GetMapping("/products/{id}/delete")
    public String deleteProduct(@PathVariable Long id) {
        productRepository.deleteById(id);
        return "redirect:/";
    }

    @GetMapping("/addProduct")
    public String addProduct() {
        return "add-product";
    }


}
