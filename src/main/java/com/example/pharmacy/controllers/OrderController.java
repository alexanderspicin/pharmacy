package com.example.pharmacy.controllers;



import com.example.pharmacy.DTO.OrderDTO;
import com.example.pharmacy.entity.User;
import com.example.pharmacy.service.OrderService;
import com.example.pharmacy.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController("/orders")
@RequestMapping("/orders")
@CrossOrigin
public class OrderController {
    private final UserService userService;
    private final OrderService orderService;

    public OrderController(UserService userService, OrderService orderService) {
        this.userService = userService;
        this.orderService = orderService;
    }

    @GetMapping("/createOrder")
    public ResponseEntity<String> createOrder(Principal principal) {
        if (principal == null) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
        User user = userService.findUserByUsername(principal.getName());
        try {
            orderService.createOrder(user);
        }catch (RuntimeException exception){
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Order created", HttpStatus.OK);
    }

    @GetMapping("/myOrders")
    public ResponseEntity<List<OrderDTO>> aboutBucket(Principal principal){
        if (principal == null){
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        } else{
            List<OrderDTO> orderDTOS = orderService.getOrdersByUser(principal.getName());
            return new ResponseEntity(orderDTOS, HttpStatus.OK);
        }
    }

    @GetMapping("/{id}")
    public  ResponseEntity<OrderDTO> getOrder(@PathVariable("id") Long id){
        OrderDTO orderDTO = orderService.getOrderBuId(id);
        return new ResponseEntity<>(orderDTO, HttpStatus.OK);
    }

}
