package com.example.midterm.controller;

import com.example.midterm.model.Cart;
import com.example.midterm.model.CartItem;
import com.example.midterm.model.User;
import com.example.midterm.repository.UserRepository;
import com.example.midterm.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class LoginController {
    @Autowired
    private ProductService productService;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/")
    public String home(Authentication authentication, Model model) {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);
        Cart cart = user.getCart();
        List<CartItem> cartItems = cart.getItems();
        cart.calculateTotal();
        model.addAttribute("total", cart.getTotal());
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("username", username);
        model.addAttribute("products", productService.getAllProducts());
        return "index";
    }

    @GetMapping("/blank")
    public String blank() {
        return "blank";
    }



    @GetMapping("/product")
    public  String product() {
        return "product-detail";
    }

    @GetMapping("/store")
    public String store() {
        return "store";
    }
}
