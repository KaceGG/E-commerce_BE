package com.E_commerceApp.controllers;


import com.E_commerceApp.DTOs.response.ApiResponse;
import com.E_commerceApp.services.ZaloPayService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/payment/zalopay")
public class ZaloPayController {

    @Autowired
    ZaloPayService zaloPayService;

    @PostMapping("/create-order")
    public ApiResponse<Map<String, Object>> createOrder(@RequestParam int amount) throws Exception {
        ApiResponse<Map<String, Object>> apiResponse = new ApiResponse<>();
        JSONObject response = zaloPayService.createOrder(amount);
        apiResponse.setResult(response.toMap());
        return apiResponse;
    }
}
