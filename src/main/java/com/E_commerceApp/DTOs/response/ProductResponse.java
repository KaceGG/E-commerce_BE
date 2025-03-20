package com.E_commerceApp.DTOs.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponse {
    private int id;
    private String name;
    private String description;
    private Float price;
    private int quantity;
    private String imageUrl;
    private Set<Integer> categoryIds;
}
