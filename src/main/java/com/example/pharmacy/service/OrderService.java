package com.example.pharmacy.service;


import com.example.pharmacy.DTO.OrderDTO;
import com.example.pharmacy.entity.User;

import java.util.List;


public interface OrderService {

    Boolean createOrder(User user);
    List<OrderDTO> getOrdersByUser(String username);

    OrderDTO getOrderBuId(Long id);
}
