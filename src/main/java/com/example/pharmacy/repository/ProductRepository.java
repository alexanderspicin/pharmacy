package com.example.pharmacy.repository;

import com.example.pharmacy.entity.Category;
import com.example.pharmacy.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Product findProductById(Long id);

    void deleteProductById(Long id);
    Product findProductByProductName(String productName);
    List<Product> findAllByCategories(Category category);

    List<Product> findAll();
}
