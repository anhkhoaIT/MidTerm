package com.example.midterm.model;

import com.example.midterm.repository.CartItemRepository;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name="carts")
@Getter @Setter @NoArgsConstructor
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name="user_id")
    private User user;
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items = new ArrayList<>();

    private double total;

    public Cart(User user) {
        this.user = user;
    }

    public void addCartItem(CartItem item) {
        items.add(item);
        item.setCart(this);
    }

    public void calculateTotal() {
        double total = 0;
        for(CartItem item : items) {
            total += item.getProduct().getPrice() * item.getQuantity();
        }
        this.total = total;
    }

}


