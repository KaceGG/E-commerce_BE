package com.E_commerceApp.services;

import com.E_commerceApp.DTOs.request.ProductCreationRequest;
import com.E_commerceApp.DTOs.request.ProductUpdateRequest;
import com.E_commerceApp.DTOs.response.ProductResponse;

import java.util.List;

public interface ProductService {
    ProductResponse getProduct(int productId);

    List<ProductResponse> getAllProducts();

    List<ProductResponse> getProductByCategory(int categoryId);

    ProductResponse createProduct(ProductCreationRequest request);

    ProductResponse updateProduct(int productId, ProductUpdateRequest request);

    void deleteProduct(int productId);
}
