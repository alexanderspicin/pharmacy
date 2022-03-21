package com.example.pharmacy.controllers;

import com.example.pharmacy.DTO.BucketDTO;
import com.example.pharmacy.service.BucketService;
import com.example.pharmacy.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController("/buckets")
@RequestMapping("/buckets")
@PreAuthorize("hasAnyRole('ADMIN', 'ClIENT')")
@CrossOrigin
public class BucketController {

    private final BucketService bucketService;
    private final ProductService productService;

    public BucketController(BucketService bucketService, ProductService productService) {
        this.bucketService = bucketService;
        this.productService = productService;
    }

    @GetMapping("/myBucket")
    public ResponseEntity<BucketDTO> aboutBucket(Principal principal){
        if (principal == null){
            return new ResponseEntity(new BucketDTO(),HttpStatus.UNAUTHORIZED);
        } else{
            BucketDTO bucketDTO = bucketService.getBucketByUser(principal.getName());
            return new ResponseEntity(bucketDTO, HttpStatus.OK);
        }
    }

    @DeleteMapping("/{productId}/remove")
    public ResponseEntity<String> deleteFromBucket(@PathVariable Long productId, Principal principal){
        try {
            productService.deleteProduct(productId, principal.getName());
        }catch (RuntimeException exception){
            return new ResponseEntity<>(exception.getMessage(),HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>("Product deleted from yours bucket",HttpStatus.OK);
    }

    @GetMapping("/add/{productId}")
    public ResponseEntity<String> addToBucket(@PathVariable Long productId, Principal principal){
        try {
            productService.addToUserBucket(productId, principal.getName());
        }catch (RuntimeException exception){
            return new ResponseEntity<>(exception.getMessage(),HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>("Product added to yours bucket",HttpStatus.OK);
    }

    @DeleteMapping("/{productId}/removeAll")
    public ResponseEntity<String> deleteFromBucketById(@PathVariable Long productId, Principal principal){
        try {
            productService.deleteAllProductById(productId, principal.getName());
        }catch (RuntimeException exception){
            return new ResponseEntity<>(exception.getMessage(),HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>("Product deleted from yours bucket",HttpStatus.OK);
    }
}
