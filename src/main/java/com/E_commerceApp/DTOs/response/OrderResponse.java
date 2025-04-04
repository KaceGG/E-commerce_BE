package com.E_commerceApp.DTOs.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponse {
    private int id;
    private LocalDateTime orderDate;
    private double totalAmount;
    private String shippingAddress;
    private String userId;
    private List<OrderItemResponse> items;
    private PaymentResponse payment;
}
