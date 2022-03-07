package com.example.pharmacy.service;

import com.example.pharmacy.DTO.BucketDTO;
import com.example.pharmacy.entity.Bucket;
import com.example.pharmacy.entity.User;

import java.util.List;

public interface BucketService {

    Bucket createBucket(User user, List<Long> productsIds);

    void addProducts(Bucket bucket, List<Long> productsIds);

    BucketDTO getBucketByUser(String username);

    void deleteProduct(Bucket bucket, Long productId);

    void deleteAllProductById(Bucket bucket, Long productId);
}
