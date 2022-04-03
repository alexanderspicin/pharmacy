package com.example.pharmacy.service;

import com.example.pharmacy.DTO.CategoryDTO;
import com.example.pharmacy.DTO.ProductDTO;
import com.example.pharmacy.entity.Product;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface ProductService {

    boolean save(ProductDTO productDTO);

    List<ProductDTO> loadProductsByCategory(CategoryDTO categoryDTO);

    ProductDTO loadProductById(Long id);

    boolean updateProductCategory(Long id, List<CategoryDTO> categoryDTOS);

    void deleteProduct(Long id, String username);

    ProductDTO loadProductByName(String name);
    void addToUserBucket(Long productId, String username);
    List<ProductDTO> loadAll();
    void deleteAllProductById(Long id, String username);
    ProductDTO productToProductDTO(Product product);

    void uploadImage(Long id, MultipartFile file);
    byte[] downloadImage(Long id);
}
