package com.E_commerceApp.DTOs.response;

import lombok.Data;

@Data
public class ZaloPayResponseDTO {
    private String orderUrl;
    private String orderToken;
    private String zpTransToken;
    private int returnCode;
    private String returnMessage;
    private double amount;
    private String appTransId;
}