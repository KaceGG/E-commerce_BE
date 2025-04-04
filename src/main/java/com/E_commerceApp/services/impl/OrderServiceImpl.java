package com.E_commerceApp.services.impl;

import com.E_commerceApp.DTOs.response.ApiResponse;
import com.E_commerceApp.DTOs.response.OrderResponse;
import com.E_commerceApp.mappers.OrderMapper;
import com.E_commerceApp.models.Order;
import com.E_commerceApp.repositories.OrderRepository;
import com.E_commerceApp.repositories.UserRepository;
import com.E_commerceApp.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderMapper orderMapper;

    public ApiResponse<List<OrderResponse>> getOrdersByUserId(String userId) {
        ApiResponse<List<OrderResponse>> apiResponse = new ApiResponse<>();
        List<Order> orders = orderRepository.findByUserId(userId);
        if (orders.isEmpty()) {
            apiResponse.setMessage("No orders found for user: " + userId);
            apiResponse.setResult(null);
        } else {
            List<OrderResponse> orderResponses = orders.stream()
                    .map(orderMapper::toOrderResponse)
                    .collect(Collectors.toList());
            apiResponse.setMessage("Orders retrieved successfully");
            apiResponse.setResult(orderResponses);
        }
        return apiResponse;
    }
}
