package com.E_commerceApp.DTOs.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PaymentResponse {
    private int id;
    private String paymentMethod;
    private Double amount; // Sử dụng Double để cho phép null
    private String status;
    private String orderToken;
    private String zpTransId;
    private String paymentUrl;
    private LocalDateTime paymentDate;
}