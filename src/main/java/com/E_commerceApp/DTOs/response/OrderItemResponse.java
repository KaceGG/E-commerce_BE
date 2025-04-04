package com.E_commerceApp.DTOs.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItemResponse {
    private int id;
    private int productId;
    private String productName; // Lấy từ Product
    private String productImageUrl; // Lấy từ Product
    private double price;
    private int quantity;
}
