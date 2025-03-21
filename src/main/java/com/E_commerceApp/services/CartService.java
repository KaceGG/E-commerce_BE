package com.E_commerceApp.services;

import com.E_commerceApp.DTOs.request.AddToCartRequest;
import com.E_commerceApp.DTOs.request.UpdateCartItemRequest;
import com.E_commerceApp.DTOs.response.CartResponse;
import com.E_commerceApp.models.Cart;

public interface CartService {
    public CartResponse getCart(String userId);

    public CartResponse addItemToCart(String userId, AddToCartRequest request);

    public CartResponse updateCartItem(int cartItemId, String userId, UpdateCartItemRequest request);

    public CartResponse removeItemFromCart(int cartItemId, String userId);

    public void clearCart(String userId);
}
