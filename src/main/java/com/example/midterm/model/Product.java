package com.example.midterm.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="products")
@NoArgsConstructor @Getter @Setter
public class Product {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String category;
    private double price;
    private String brand;
    private String color;
    private String image;

    public Product(String name, String category, double price, String brand, String color, String image) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.brand = brand;
        this.color = color;
        this.image = image;
    }

}
