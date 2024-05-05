package com.example.midterm.service;

import com.example.midterm.model.Cart;
import com.example.midterm.model.Product;

public interface CartService {
    public void incrementQuantity(Cart cart, Product product);
    public void decrementQuantity(Cart cart, Product product);
}
