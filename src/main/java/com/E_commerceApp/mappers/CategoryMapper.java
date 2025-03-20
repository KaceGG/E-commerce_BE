package com.E_commerceApp.mappers;

import com.E_commerceApp.DTOs.request.CategoryCreationRequest;
import com.E_commerceApp.DTOs.request.CategoryUpdateRequest;
import com.E_commerceApp.DTOs.response.CategoryResponse;
import com.E_commerceApp.models.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category toCategory(CategoryCreationRequest request);

    CategoryResponse toCategoryResponse(Category category);

    void updateCategory(@MappingTarget Category category, CategoryUpdateRequest request);
}
