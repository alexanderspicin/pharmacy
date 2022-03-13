package com.example.pharmacy.service;


import com.example.pharmacy.DTO.BucketDTO;
import com.example.pharmacy.DTO.BucketDetailDTO;
import com.example.pharmacy.DTO.OrderDTO;
import com.example.pharmacy.DTO.OrderDetailDTO;
import com.example.pharmacy.entity.*;
import com.example.pharmacy.repository.BucketRepository;
import com.example.pharmacy.repository.OrderRepository;
import com.example.pharmacy.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class OrderServiceImpl implements OrderService{
    private final OrderRepository orderRepository;
    private final BucketRepository bucketRepository;
    private final UserRepository userRepository;

    public OrderServiceImpl(OrderRepository orderRepository, BucketRepository bucketRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.bucketRepository = bucketRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public Boolean createOrder(User user) {
        if (user.getBucket()==null || user.getBucket().getProducts().isEmpty()){
            throw  new RuntimeException("Your bucket is empty");
        }
        List<Product> bucketProductList = user.getBucket().getProducts();
        Order order = Order.builder().status(Status.NEW).products(bucketProductList).user(user).build();
        orderRepository.save(order);
        Bucket bucket = user.getBucket();
        bucket.setProducts(null);
        bucketRepository.save(bucket);
        return true;
    }

    @Override
    public List<OrderDTO> getOrdersByUser(String username) {

        List<OrderDTO> orderDTOS = new ArrayList<>();
        User user = userRepository.findUserByUsername(username);
        if (user == null || orderRepository.findAllByUser(user).isEmpty()) {
            return orderDTOS;
        }
        for (Order order : orderRepository.findAllByUser(user)) {
            OrderDTO orderDTO = new OrderDTO();
            Map<Long, OrderDetailDTO> mapByProductId = new HashMap<>();
            List<Product> products = order.getProducts();
            for (Product product : products) {
                OrderDetailDTO detail = mapByProductId.get(product.getId());
                if (detail == null) {
                    mapByProductId.put(product.getId(), new OrderDetailDTO(product));
                } else {
                    detail.setAmount(detail.getAmount() + 1);
                    detail.setSum(detail.getSum() + Double.valueOf(product.getPrice().toString()));
                }
            }
            orderDTO.setStatus(order.getStatus());
            orderDTO.setCreateTime(order.getCreateTime());
            orderDTO.setOrderDetails(new ArrayList<>(mapByProductId.values()));
            orderDTO.aggregate();
            orderDTOS.add(orderDTO);
        }
        return orderDTOS;
    }
}
