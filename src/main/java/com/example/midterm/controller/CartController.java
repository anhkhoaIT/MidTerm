package com.example.midterm.controller;

import com.example.midterm.model.*;
import com.example.midterm.repository.CartItemRepository;
import com.example.midterm.repository.CartRepository;
import com.example.midterm.repository.OrderRepository;
import com.example.midterm.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class CartController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private OrderRepository orderRepository;

    @GetMapping("/cart")
    public String cart(Authentication authentication, Model model) {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);
        Cart cart = user.getCart();
        cart.calculateTotal();
        model.addAttribute("total", cart.getTotal());
        model.addAttribute("cartItems", cart.getItems());
        model.addAttribute("username", username);
        return "cart";
    }

    @GetMapping("/cart/delete/{id}")
    public String deleteCartItem(@PathVariable Long id, Authentication authentication) {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);
        CartItem cartItem = cartItemRepository.findById(id).orElse(null);
        if(cartItem == null) {
            return "redirect:/cart";
        }

        Cart cart = user.getCart();
        cart.getItems().remove(cartItem);
        cartRepository.save(cart);
        return "redirect:/cart";
    }

    @GetMapping("/checkout")
    public String checkout(Authentication authentication, Model model) {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);
        Cart cart = user.getCart();
        List<CartItem> cartItems = cart.getItems();
        cart.calculateTotal();
        model.addAttribute("user", user);
        model.addAttribute("total", cart.getTotal());
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("username", username);
        return "checkout";
    }

    @GetMapping("/checkout/process")
    public String checkoutSuccess(Authentication authentication) {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);
        Cart cart = user.getCart();

        // Create a new Order for the User
        Order order = new Order(user);

        // Convert CartItems to OrderItems and add them to the Order
        for (CartItem cartItem : cart.getItems()) {
            OrderItem orderItem = new OrderItem(cartItem.getProduct(), cartItem.getQuantity());
            orderItem.setOrder(order);
            order.getItems().add(orderItem);
        }

        // Save the Order to the database
        orderRepository.save(order);

        // Clear the Cart
        cart.getItems().clear();
        cartRepository.save(cart);

        return "redirect:/order";
    }

    @GetMapping("/order")
    public String order(Authentication authentication, Model model) {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);

        List<Order> orders = user.getOrders();
        model.addAttribute("orders", orders);
        model.addAttribute("username", username);
        return "order";
    }
}
