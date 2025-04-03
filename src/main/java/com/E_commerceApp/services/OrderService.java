package com.E_commerceApp.services;

import com.E_commerceApp.DTOs.request.OrderRequest;
import com.E_commerceApp.DTOs.response.OrderResponse;

public interface OrderService {
    public OrderResponse createOrder(OrderRequest request);
}
