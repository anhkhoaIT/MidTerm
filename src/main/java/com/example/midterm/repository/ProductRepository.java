package com.example.midterm.repository;

import com.example.midterm.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long>{
    public Iterable<Product> findByCategory(String category);
    @Query(value = "SELECT * FROM products WHERE to_tsvector('english', category || ' ' || brand || ' ' || color) @@ to_tsquery(:keyword) AND (price BETWEEN :minPrice AND :maxPrice)", nativeQuery = true)
    List<Product> search(@Param("keyword") String keyword, @Param("minPrice") Double minPrice, @Param("maxPrice") Double maxPrice);

}
