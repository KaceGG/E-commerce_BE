package com.E_commerceApp.services;

import com.E_commerceApp.DTOs.request.CategoryCreationRequest;
import com.E_commerceApp.DTOs.response.CategoryResponse;

public interface CategoryService {
    public CategoryResponse createCategory(CategoryCreationRequest request);
}
