package com.example.pharmacy.controllers;

import com.example.pharmacy.DTO.CategoryDTO;
import com.example.pharmacy.DTO.ProductDTO;
import com.example.pharmacy.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController("/products")
@RequestMapping("/products")
@CrossOrigin
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProduct(@PathVariable(name = "id") Long id) {
        try {
            ProductDTO productDTO = productService.loadProductById(id);
            return new ResponseEntity<>(productDTO, HttpStatus.OK);
        } catch (RuntimeException exception) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/getAllProducts")
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<ProductDTO> productsDTO = productService.loadAll();
        return new ResponseEntity<>(productsDTO, HttpStatus.OK);
    }

    @GetMapping("{productId}/image/download")
    public ResponseEntity<byte[]> downloadProductImage(@PathVariable("productId") Long id){
        byte[] image = productService.downloadImage(id);
        return new ResponseEntity<>(image, HttpStatus.OK);

    }

    @PostMapping("/getProductByCategory")
    public ResponseEntity<List<ProductDTO>> getProductByCategory(@RequestBody CategoryDTO categoryDTO){
        List<ProductDTO> productDTOS = productService.loadProductsByCategory(categoryDTO);
        return new ResponseEntity<>(productDTOS, HttpStatus.OK);
    }
}
