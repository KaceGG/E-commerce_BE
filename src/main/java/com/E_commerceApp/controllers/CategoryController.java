package com.E_commerceApp.controllers;

import com.E_commerceApp.DTOs.request.CategoryCreationRequest;
import com.E_commerceApp.DTOs.request.CategoryUpdateRequest;
import com.E_commerceApp.DTOs.response.ApiResponse;
import com.E_commerceApp.DTOs.response.CategoryResponse;
import com.E_commerceApp.repositories.CategoryRepository;
import com.E_commerceApp.services.CategoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
@CrossOrigin("*")
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryService categoryService, CategoryRepository categoryRepository) {
        this.categoryService = categoryService;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("/getAll")
    public ApiResponse<List<CategoryResponse>> getCategories() {
        ApiResponse<List<CategoryResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(categoryService.getCategories());
        return apiResponse;
    }

    @GetMapping("/{categoryId}")
    public CategoryResponse getCategory(@PathVariable int categoryId) {
        return categoryService.getCategory(categoryId);
    }

    @PostMapping("/create")
    public ApiResponse<CategoryResponse> create(@RequestBody CategoryCreationRequest request) {
        ApiResponse<CategoryResponse> response = new ApiResponse<>();
        response.setResult(categoryService.createCategory(request));
        return response;
    }

    @PutMapping("/update/{categoryId}")
    public ApiResponse<CategoryResponse> updateCategory(@PathVariable int categoryId,
                                                        @RequestBody CategoryUpdateRequest request) {
        ApiResponse<CategoryResponse> response = new ApiResponse<>();
        response.setResult(categoryService.updateCategory(categoryId, request));
        return response;
    }

    @DeleteMapping("/delete/{categoryId}")
    public ApiResponse<Void> deleteCategory(@PathVariable int categoryId) {
        categoryService.deleteCategory(categoryId);
        ApiResponse<Void> response = new ApiResponse<>();
        response.setMessage("Category deleted successfully!");
        return response;
    }
}
