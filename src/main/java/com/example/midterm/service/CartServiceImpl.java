package com.example.midterm.service;

import com.example.midterm.model.Cart;
import com.example.midterm.model.CartItem;
import com.example.midterm.model.Product;
import com.example.midterm.repository.CartItemRepository;
import com.example.midterm.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;

@Service
public class CartServiceImpl implements CartService{
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private CartRepository cartRepository;
    @Override
    public void incrementQuantity(Cart cart, Product product) {
        for(CartItem item : cart.getItems()) {
            if(item.getProduct().getId().equals(product.getId()) && item.getCart().getId().equals(cart.getId())) {
                item.incrementQuantity();
                cartItemRepository.save(item);
                return;
            }
        }
        CartItem newItem = new CartItem(product);
        cart.addCartItem(newItem);

        cartItemRepository.save(newItem);
        cartRepository.save(cart);
    }

    @Override
    public void decrementQuantity(Cart cart, Product product) {
        Iterator<CartItem> iterator = cart.getItems().iterator();
        while (iterator.hasNext()) {
            CartItem item = iterator.next();
            if (item.getProduct().getId().equals(product.getId())) {
                item.decrementQuantity();
                if (item.getQuantity() == 0) {
                    iterator.remove();
                }
                return;
            }
        }
    }
}
