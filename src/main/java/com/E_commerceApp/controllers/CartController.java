package com.E_commerceApp.controllers;

import com.E_commerceApp.DTOs.request.AddToCartRequest;
import com.E_commerceApp.DTOs.request.UpdateCartItemRequest;
import com.E_commerceApp.DTOs.response.ApiResponse;
import com.E_commerceApp.DTOs.response.CartResponse;
import com.E_commerceApp.services.CartService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
@CrossOrigin("*")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public ApiResponse<CartResponse> getCart(@RequestParam("userId") String userId) {
        ApiResponse<CartResponse> apiResponse = new ApiResponse<>();
        apiResponse.setCode(1000);
        apiResponse.setMessage("Cart retrieved successfully");
        apiResponse.setResult(cartService.getCart(userId));
        return apiResponse;
    }

    @PostMapping("/add")
    public ApiResponse<CartResponse> addToCart(
            @RequestParam("userId") String userId,
            @Valid @RequestBody AddToCartRequest request) {
        ApiResponse<CartResponse> apiResponse = new ApiResponse<>();
        apiResponse.setCode(1000);
        apiResponse.setMessage("Product added to cart successfully");
        apiResponse.setResult(cartService.addItemToCart(userId, request));
        return apiResponse;
    }

    @PutMapping("/update/{cartItemId}")
    public ApiResponse<CartResponse> updateCartItem(
            @RequestParam("userId") String userId,
            @PathVariable("cartItemId") int cartItemId,
            @Valid @RequestBody UpdateCartItemRequest request) {
        ApiResponse<CartResponse> apiResponse = new ApiResponse<>();
        apiResponse.setCode(1000);
        apiResponse.setMessage("Cart item updated successfully");
        apiResponse.setResult(cartService.updateCartItem(cartItemId, userId, request));
        return apiResponse;
    }

    @DeleteMapping("/remove/{cartItemId}")
    public ApiResponse<CartResponse> removeFromCart(
            @RequestParam("userId") String userId,
            @PathVariable("cartItemId") int cartItemId) {
        ApiResponse<CartResponse> apiResponse = new ApiResponse<>();
        apiResponse.setCode(1000);
        apiResponse.setMessage("Cart item removed successfully");
        apiResponse.setResult(cartService.removeItemFromCart(cartItemId, userId));
        return apiResponse;
    }

    @DeleteMapping("/clear")
    public ApiResponse<String> clearCart(@RequestParam("userId") String userId) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        cartService.clearCart(userId);
        apiResponse.setCode(1000);
        apiResponse.setMessage("Cart cleared successfully");
        apiResponse.setResult("Cart for user ID " + userId + " has been cleared");
        return apiResponse;
    }
}
