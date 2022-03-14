package com.example.pharmacy.repository;

import com.example.pharmacy.entity.Order;
import com.example.pharmacy.entity.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {


    List<Order> findAllByUser(User user);

    Order findOrderById(Long id);
}
