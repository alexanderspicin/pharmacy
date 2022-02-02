package com.example.pharmacy.service;

import com.example.pharmacy.DTO.ProductDTO;
import com.example.pharmacy.entity.Product;

import java.util.List;


public interface ProductService {

    boolean save(ProductDTO productDTO);

    ProductDTO loadProductById(Long id);

    void deleteProduct(Long id);

    List<ProductDTO> loadAll();

    ProductDTO productToProductDTO(Product product);
}
