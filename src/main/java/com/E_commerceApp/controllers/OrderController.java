package com.E_commerceApp.controllers;

import com.E_commerceApp.DTOs.request.OrderRequest;
import com.E_commerceApp.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/order")
public class OrderController {
    @Autowired
    OrderRepository orderRepository;

    @PostMapping("/create")
    public ResponseEntity<?> createOrder(@RequestBody OrderRequest orderRequest) throws Exception {
        return null;
    }
}
