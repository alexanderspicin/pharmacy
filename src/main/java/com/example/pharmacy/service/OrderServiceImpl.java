package com.example.pharmacy.service;

import com.example.pharmacy.DTO.OrderDTO;
import com.example.pharmacy.DTO.OrderDetailDTO;
import com.example.pharmacy.entity.*;
import com.example.pharmacy.repository.BucketRepository;
import com.example.pharmacy.repository.OrderRepository;
import com.example.pharmacy.repository.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.util.*;


@Service
public class OrderServiceImpl implements OrderService{
    private final OrderRepository orderRepository;
    private final BucketRepository bucketRepository;
    private final UserRepository userRepository;
    private final EmailSender emailSender;

    public OrderServiceImpl(OrderRepository orderRepository, BucketRepository bucketRepository, UserRepository userRepository, EmailSender emailSender) {
        this.orderRepository = orderRepository;
        this.bucketRepository = bucketRepository;
        this.userRepository = userRepository;
        this.emailSender = emailSender;
    }

    @Override
    public void changeOrderStatus(Long orderId, String status) {
        try{
            Status.valueOf(status);
        }catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
            /* LOG IT*/
            throw new RuntimeException("Status with name: "+ status + " not found");
        }
        Order order = orderRepository.findOrderById(orderId);
        order.setStatus(Status.valueOf(status));
        orderRepository.save(order);
        try {
            emailSender.sendTrackEmail(order);
        } catch (MessagingException e) {
            /* LOG IT*/
        }
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
        return getOrderDTOS(orderDTOS, user);
    }

    private List<OrderDTO> getOrderDTOS(List<OrderDTO> orderDTOS, User user) {
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
            orderDTO.setId(order.getId());
            orderDTOS.add(orderDTO);
        }
        return orderDTOS;
    }

    @Override
    public List<OrderDTO> getOrdersByUserId(Long id) {

        List<OrderDTO> orderDTOS = new ArrayList<>();
        User user = userRepository.findUserById(id);
        if (user == null || orderRepository.findAllByUserId(id).isEmpty()) {
            return orderDTOS;
        }
        return getOrderDTOS(orderDTOS, user);
    }

    @Override
    public OrderDTO getOrderBuId(Long id) {
        Order order = orderRepository.findOrderById(id);
        if (order == null){
            throw new RuntimeException("Can't find order with id: " + id);
        }
        OrderDTO orderDTO = new OrderDTO();
        Map<Long, OrderDetailDTO> mapByProductId = new HashMap<>();
        List<Product> products = orderRepository.findOrderById(id).getProducts();
        for(Product product : products){
            OrderDetailDTO detail = mapByProductId.get(product.getId());
            if (detail == null){
                mapByProductId.put(product.getId(), new OrderDetailDTO(product));
            }else{
                detail.setAmount(detail.getAmount() + 1);
                detail.setSum(detail.getSum() + Double.valueOf(product.getPrice().toString()));
            }
        }
        orderDTO.setId(order.getId());
        orderDTO.setCreateTime(order.getCreateTime());
        orderDTO.setStatus(order.getStatus());
        orderDTO.setOrderDetails(new ArrayList<>(mapByProductId.values()));
        orderDTO.aggregate();
        return orderDTO;
    }
}
