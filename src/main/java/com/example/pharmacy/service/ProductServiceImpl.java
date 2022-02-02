package com.example.pharmacy.service;

import com.example.pharmacy.DTO.ProductDTO;
import com.example.pharmacy.entity.Product;
import com.example.pharmacy.repository.ProductRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;


@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public boolean save(ProductDTO productDTO) throws DataIntegrityViolationException {
        Product product = Product.builder()
                .productName(productDTO.getProductName())
                .productDescription(productDTO.getProductDescription())
                .composition(productDTO.getComposition())
                .indications(productDTO.getIndications())
                .price(productDTO.getPrice())
                .manifacturer(productDTO.getManifacturer()).build();

        productRepository.save(product);
        return true;
    }

    @Override
    public ProductDTO loadProductById(Long id) {
        Product product = productRepository.findProductById(id);
        if (product == null) {
            throw new RuntimeException("Product not found with id: " + id);
        }
        return new ProductDTO(product.getProductName(),
                product.getProductDescription(),
                product.getPrice(),
                product.getManifacturer(),
                product.getComposition(),
                product.getIndications());
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteProductById(id);
    }

    @Override
    public List<ProductDTO> loadAll() {
        return productRepository.findAll().stream().map(this::productToProductDTO).collect(Collectors.toList());
    }

    @Override
    public ProductDTO productToProductDTO(Product product) {
        return ProductDTO.builder()
                .productName(product.getProductName())
                .composition(product.getComposition())
                .indications(product.getIndications())
                .productDescription(product.getProductDescription())
                .manifacturer(product.getManifacturer())
                .price(product.getPrice())
                .build();
    }
}
