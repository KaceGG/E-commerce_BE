package com.E_commerceApp.controllers;

import com.E_commerceApp.DTOs.request.ProductCreationRequest;
import com.E_commerceApp.DTOs.request.ProductUpdateRequest;
import com.E_commerceApp.DTOs.response.ApiResponse;
import com.E_commerceApp.DTOs.response.ProductResponse;
import com.E_commerceApp.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
@CrossOrigin("*")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/getAll")
    public ApiResponse<List<ProductResponse>> getAllProducts() {
        ApiResponse<List<ProductResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(productService.getAllProducts());
        return apiResponse;
    }

    @GetMapping("/{productId}")
    public ApiResponse<ProductResponse> getProductById(@PathVariable int productId) {
        ApiResponse<ProductResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(productService.getProduct(productId));
        return apiResponse;
    }

    @PostMapping(value = "/create", consumes = "multipart/form-data")
    public ApiResponse<ProductResponse> createProduct(@Valid @ModelAttribute ProductCreationRequest request) {
        ApiResponse<ProductResponse> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Product created successfully");
        apiResponse.setResult(productService.createProduct(request));
        return apiResponse;
    }

    @PutMapping(value = "/update/{productId}", consumes = "multipart/form-data")
    public ApiResponse<ProductResponse> updateProduct(
            @PathVariable int productId,
            @Valid @ModelAttribute ProductUpdateRequest request) {
        ApiResponse<ProductResponse> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Product updated successfully");
        apiResponse.setResult(productService.updateProduct(productId, request));
        return apiResponse;
    }

    @DeleteMapping("/delete/{productId}")
    public ApiResponse<Void> deleteProduct(@PathVariable int productId) {
        productService.deleteProduct(productId);
        ApiResponse<Void> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Product deleted successfully");
        return apiResponse;
    }
}
