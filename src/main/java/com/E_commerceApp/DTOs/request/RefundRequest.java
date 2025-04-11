package com.E_commerceApp.DTOs.request;

import lombok.Data;

@Data
public class RefundRequest {
    private Integer orderId;
    private String zpTransId;  // Mã giao dịch ZaloPay
    private double amount;      // Số tiền cần hoàn
    private String reason;
}
