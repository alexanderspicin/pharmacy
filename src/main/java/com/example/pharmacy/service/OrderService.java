package com.example.pharmacy.service;


import com.example.pharmacy.DTO.OrderDTO;
import com.example.pharmacy.entity.User;

import java.util.List;


public interface OrderService {

    void changeOrderStatus(Long orderId, String status);
    Boolean createOrder(User user);
    List<OrderDTO> getOrdersByUser(String username);
    List<OrderDTO> getOrdersByUserId(Long id);
    OrderDTO getOrderBuId(Long id);
}
