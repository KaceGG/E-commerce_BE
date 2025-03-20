package com.E_commerceApp.services;

import com.E_commerceApp.DTOs.request.CategoryCreationRequest;
import com.E_commerceApp.DTOs.request.CategoryUpdateRequest;
import com.E_commerceApp.DTOs.response.CategoryResponse;

import java.util.List;

public interface CategoryService {
    public CategoryResponse getCategory(int categoryId);

    public List<CategoryResponse> getCategories();

    public CategoryResponse createCategory(CategoryCreationRequest request);

    public CategoryResponse updateCategory(int categoryId, CategoryUpdateRequest request);

    public void deleteCategory(int categoryId);
}
