package com.E_commerceApp.mappers;

import com.E_commerceApp.DTOs.request.ProductCreationRequest;
import com.E_commerceApp.DTOs.request.ProductUpdateRequest;
import com.E_commerceApp.DTOs.response.CartItemResponse;
import com.E_commerceApp.DTOs.response.CartResponse;
import com.E_commerceApp.DTOs.response.ProductResponse;
import com.E_commerceApp.models.Cart;
import com.E_commerceApp.models.CartItem;
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

    @Mapping(source = "cartItems", target = "cartItems")
    @Mapping(target = "totalPrice", expression = "java(calculateTotalPrice(cart))")
    CartResponse toCartResponse(Cart cart);

    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.name", target = "productName")
    @Mapping(source = "product.price", target = "productPrice")
    @Mapping(source = "product.imageUrl", target = "productImageUrl")
    CartItemResponse toCartItemResponse(CartItem cartItem);

    default float calculateTotalPrice(Cart cart) {
        if (cart == null || cart.getCartItems() == null) {
            return 0.0f;
        }
        return (float) cart.getCartItems().stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();
    }
}
