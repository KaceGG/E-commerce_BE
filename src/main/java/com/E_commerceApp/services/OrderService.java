package com.E_commerceApp.services;

import com.E_commerceApp.DTOs.response.ApiResponse;
import com.E_commerceApp.DTOs.response.OrderResponse;

import java.util.List;

public interface OrderService {
//    public OrderResponse createOrder(OrderRequest request);

    public ApiResponse<List<OrderResponse>> getOrdersByUserId(String userId);
}
