package com.example.pharmacy.repository;

import com.example.pharmacy.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Product findProductById(Long id);

    void deleteProductById(Long id);

    List<Product> findAll();
}
