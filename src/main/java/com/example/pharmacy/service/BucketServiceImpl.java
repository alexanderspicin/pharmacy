package com.example.pharmacy.service;

import com.example.pharmacy.DTO.BucketDTO;
import com.example.pharmacy.DTO.BucketDetailDTO;
import com.example.pharmacy.entity.Bucket;
import com.example.pharmacy.entity.Product;
import com.example.pharmacy.entity.User;
import com.example.pharmacy.repository.BucketRepository;
import com.example.pharmacy.repository.ProductRepository;
import com.example.pharmacy.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BucketServiceImpl implements BucketService{

    private final BucketRepository bucketRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public BucketServiceImpl(BucketRepository bucketRepository, ProductRepository productRepository, UserRepository userRepository) {
        this.bucketRepository = bucketRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public Bucket createBucket(User user, List<Long> productsIds) {
        Bucket bucket = new Bucket();
        bucket.setUser(user);
        List<Product> productList = getCollectRefProductByIds(productsIds);
        bucket.setProducts(productList);
        return bucketRepository.save(bucket);
    }

    private List<Product> getCollectRefProductByIds(List<Long> productsIds) {
        return productsIds.stream().map(productRepository::getOne).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void addProducts(Bucket bucket, List<Long> productsIds) {
        List<Product> products = bucket.getProducts();
        List<Product> newProductList = products == null ? new ArrayList<>(): new ArrayList<>(products);
        newProductList.addAll(getCollectRefProductByIds(productsIds));
        bucket.setProducts(newProductList);
        bucketRepository.save(bucket);
    }

    @Override
    @Transactional
    public void deleteProduct(Bucket bucket, Long productsIds) {
        List<Product> products = bucket.getProducts();
        products.remove(productRepository.findProductById(productsIds));
        bucket.setProducts(products);
        bucketRepository.save(bucket);
    }

    @Override
    @Transactional
    public void deleteAllProductById(Bucket bucket, Long productsIds) {
        List<Product> products = bucket.getProducts();
        products.removeAll(getCollectRefProductByIds(Collections.singletonList(productsIds)));
        bucket.setProducts(products);
        bucketRepository.save(bucket);
    }

    @Override
    public BucketDTO getBucketByUser(String username) {
        User user = userRepository.findUserByUsername(username);
        if (user == null || user.getBucket() == null){
            return new BucketDTO();
        }
        BucketDTO bucketDTO = new BucketDTO();
        Map<Long, BucketDetailDTO> mapByProductId = new HashMap<>();
        List<Product> products = user.getBucket().getProducts();
        for(Product product : products){
            BucketDetailDTO detail = mapByProductId.get(product.getId());
            if (detail == null){
                mapByProductId.put(product.getId(), new BucketDetailDTO(product));
            }else{
                detail.setAmount(detail.getAmount() + 1);
                detail.setSum(detail.getSum() + Double.valueOf(product.getPrice().toString()));
            }
        }
        bucketDTO.setBucketDetails(new ArrayList<>(mapByProductId.values()));
        bucketDTO.aggregate();
        return bucketDTO;
    }
}
