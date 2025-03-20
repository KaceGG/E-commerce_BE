package com.E_commerceApp.mappers;

import com.E_commerceApp.DTOs.request.ProductCreationRequest;
import com.E_commerceApp.DTOs.request.ProductUpdateRequest;
import com.E_commerceApp.DTOs.response.ProductResponse;
import com.E_commerceApp.models.Category;
import com.E_commerceApp.models.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(target = "imageUrl", ignore = true)
    Product toProduct(ProductCreationRequest request);

    @Mapping(source = "categories", target = "categoryIds", qualifiedByName = "mapCategoriesToIds")
    ProductResponse toProductResponse(Product product);

    @Named("mapCategoriesToIds")
    default Set<Integer> mapCategoriesToIds(Set<Category> categories) {
        if (categories == null) {
            return null;
        }
        return categories.stream()
                .map(Category::getId)
                .collect(Collectors.toSet());
    }

    @Mapping(target = "imageUrl", ignore = true)
    void updateProduct(ProductUpdateRequest request, @MappingTarget Product product);
}
