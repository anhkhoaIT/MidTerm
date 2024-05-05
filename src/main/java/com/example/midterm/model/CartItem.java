package com.example.midterm.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="cart_items", uniqueConstraints = @UniqueConstraint(columnNames = {"product_id", "cart_id"}))
@NoArgsConstructor @Getter @Setter
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cart_items_seq")
    @SequenceGenerator(name = "cart_items_seq", sequenceName = "cart_items_seq", allocationSize = 1)
    private Long id;
    private int quantity;
    @ManyToOne
    @JoinColumn(name="product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name="cart_id")
    private Cart cart;



    public CartItem(Product product) {
        this.product = product;
        this.quantity = 1;
    }

    public void incrementQuantity() {
        this.quantity++;
    }

    public void decrementQuantity() {
        if(this.quantity > 0) {
            this.quantity--;
        }

    }

//    public void calculateTotal() {
//        if (this.product != null) {
//            this.total = this.quantity * this.product.getPrice();
//        }
//    }


}
