package com.example.midterm.service;

import com.example.midterm.model.Product;

import java.util.List;

public interface ProductService {
    public List<Product> getAllProducts();
    public Product getProductById(Long id);
    public List<Product> getProductsByCategory(String category);}
