package com.E_commerceApp.controllers;

import com.E_commerceApp.DTOs.request.RefundRequest;
import com.E_commerceApp.DTOs.response.ApiResponse;
import com.E_commerceApp.DTOs.response.OrderResponse;
import com.E_commerceApp.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public ApiResponse<List<OrderResponse>> getOrdersByUserId(
            @RequestParam("userId") String userId) {
        return orderService.getOrdersByUserId(userId);
    }

//    @PostMapping("/cancel")
//    public ApiResponse<OrderResponse> cancelOrder(@RequestBody RefundRequest request) throws Exception {
//        ApiResponse<OrderResponse> apiResponse = new ApiResponse<>();
//        apiResponse.setResult(orderService.cancelOrder(request));
//        return apiResponse;
//    }
}
