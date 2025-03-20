package com.E_commerceApp.DTOs.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductCreationRequest {
    @NotBlank(message = "Product name is required!")
    private String name;

    private String description;
    private Float price;
    private int quantity;
    private MultipartFile image;
    private Set<Integer> categoryIds;
}
