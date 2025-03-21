package com.E_commerceApp.DTOs.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartItemResponse {
    private int id;
    private int productId;
    private String productName;
    private Float productPrice;
    private String productImageUrl;
    private int quantity;
}
