package com.E_commerceApp.services;

import com.E_commerceApp.DTOs.response.ApiResponse;
import com.E_commerceApp.DTOs.response.OrderResponse;

import java.util.List;

public interface OrderService {
    ApiResponse<List<OrderResponse>> getAllOrders();

    ApiResponse<List<OrderResponse>> getOrdersByUserId(String userId);

    Void cancelOrder(int orderId, String userId);
}

