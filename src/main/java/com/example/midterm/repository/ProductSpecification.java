package com.example.midterm.repository;

import com.example.midterm.model.Product;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpecification {
    public static Specification<Product> hasCategory(String category) {
        return (product, cq, cb) -> category == null ? null : cb.equal(product.get("category"), category);
    }

    public static Specification<Product> hasBrand(String brand) {
        return (product, cq, cb) -> brand == null ? null : cb.equal(product.get("brand"), brand);
    }

    public static Specification<Product> hasColor(String color) {
        return (product, cq, cb) -> color == null ? null : cb.equal(product.get("color"), color);
    }

    public static Specification<Product> hasPriceBetween(Double minPrice, Double maxPrice) {
        return (product, cq, cb) -> minPrice == null || maxPrice == null ? null : cb.between(product.get("price"), minPrice, maxPrice);
    }
}
