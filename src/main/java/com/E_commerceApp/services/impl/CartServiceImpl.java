package com.E_commerceApp.services.impl;

import com.E_commerceApp.DTOs.request.AddToCartRequest;
import com.E_commerceApp.DTOs.request.UpdateCartItemRequest;
import com.E_commerceApp.DTOs.response.CartResponse;
import com.E_commerceApp.exception.AppException;
import com.E_commerceApp.exception.ErrorCode;
import com.E_commerceApp.mappers.ProductMapper;
import com.E_commerceApp.models.Cart;
import com.E_commerceApp.models.CartItem;
import com.E_commerceApp.models.Product;
import com.E_commerceApp.models.User;
import com.E_commerceApp.repositories.CartItemRepository;
import com.E_commerceApp.repositories.CartRepository;
import com.E_commerceApp.repositories.ProductRepository;
import com.E_commerceApp.repositories.UserRepository;
import com.E_commerceApp.services.CartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class CartServiceImpl implements CartService {
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductMapper productMapper;

    public CartServiceImpl(UserRepository userRepository, CartRepository cartRepository, ProductRepository productRepository, CartItemRepository cartItemRepository, ProductMapper productMapper) {
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.cartItemRepository = cartItemRepository;
        this.productMapper = productMapper;
    }

    @Transactional
    @Override
    public CartResponse getCart(String userId) {
        // Tìm user và giỏ hàng
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Cart cart = user.getCart();
        if (cart == null) {
            // Tạo giỏ hàng mới nếu chưa có
            cart = new Cart();
            cart.setUser(user);
            cart = cartRepository.save(cart);
            user.setCart(cart);
            userRepository.save(user);
        }
        return productMapper.toCartResponse(cart);
    }

    @Transactional
    @Override
    public CartResponse addItemToCart(String userId, AddToCartRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // Tìm hoặc tạo giỏ hàng cho user
        Cart cart = user.getCart();
        if (cart == null) {
            cart = new Cart();
            cart.setUser(user);
            cart = cartRepository.save(cart);
            user.setCart(cart);
            userRepository.save(user);
        }

        // Tìm sản phẩm
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        // Kiểm tra số lượng tồn kho
        if (product.getQuantity() < request.getQuantity()) {
            throw new AppException(ErrorCode.INSUFFICIENT_STOCK);
        }

        // Kiểm tra xem sản phẩm đã có trong giỏ hàng chưa
        CartItem existingItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId() == request.getProductId())
                .findFirst()
                .orElse(null);

        if (existingItem != null) {
            // Nếu sản phẩm đã có, cập nhật số lượng
            existingItem.setQuantity(existingItem.getQuantity() + request.getQuantity());
            cartItemRepository.save(existingItem);
        } else {
            // Nếu chưa có, tạo mới CartItem
            CartItem cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(request.getQuantity());
            cart.getCartItems().add(cartItem);
            cartItemRepository.save(cartItem);
        }

        // Lưu giỏ hàng
        cartRepository.save(cart);
        log.info("Added product ID {} to cart for user ID: {}", request.getProductId(), userId);

        return productMapper.toCartResponse(cart);
    }

    @Transactional
    @Override
    public CartResponse updateCartItem(int cartItemId, String userId, UpdateCartItemRequest request) {
        log.info("Updating cart item ID: {} for user ID: {}", cartItemId, userId);

        // Tìm user và giỏ hàng
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Cart cart = user.getCart();
        if (cart == null) {
            throw new AppException(ErrorCode.CART_NOT_FOUND);
        }

        // Tìm cart item
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new AppException(ErrorCode.CART_ITEM_NOT_FOUND));

        // Kiểm tra xem cart item có thuộc về giỏ hàng của user không
        if (cartItem.getCart().getId() != cart.getId()) {
            throw new AppException(ErrorCode.CART_ITEM_NOT_FOUND);
        }

        // Kiểm tra số lượng tồn kho
        Product product = cartItem.getProduct();
        if (product.getQuantity() < request.getQuantity()) {
            throw new AppException(ErrorCode.INSUFFICIENT_STOCK);
        }

        // Cập nhật số lượng
        cartItem.setQuantity(request.getQuantity());
        cartItemRepository.save(cartItem);
        log.info("Updated cart item ID: {} with quantity: {}", cartItemId, request.getQuantity());

        return productMapper.toCartResponse(cart);
    }

    @Transactional
    @Override
    public CartResponse removeItemFromCart(int cartItemId, String userId) {
        log.info("Removing cart item ID: {} for user ID: {}", cartItemId, userId);

        // Tìm user và giỏ hàng
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Cart cart = user.getCart();
        if (cart == null) {
            throw new AppException(ErrorCode.CART_NOT_FOUND);
        }

        // Tìm cart item
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new AppException(ErrorCode.CART_ITEM_NOT_FOUND));

        // Kiểm tra xem cart item có thuộc về giỏ hàng của user không
        if (cartItem.getCart().getId() != cart.getId()) {
            throw new AppException(ErrorCode.CART_ITEM_NOT_FOUND);
        }

        // Xóa cart item
        cart.getCartItems().remove(cartItem);
        cartItemRepository.delete(cartItem);
        log.info("Removed cart item ID: {} from cart for user ID: {}", cartItemId, userId);

        return productMapper.toCartResponse(cart);
    }

    @Transactional
    @Override
    public void clearCart(String userId) {
        log.info("Clearing cart for user ID: {}", userId);

        // Tìm user và giỏ hàng
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        Cart cart = user.getCart();
        if (cart == null) {
            log.info("No cart found for user ID: {}", userId);
            return;
        }

        // Xóa tất cả cart items
        cart.getCartItems().clear();
        cartRepository.save(cart);
        log.info("Cleared cart for user ID: {}", userId);
    }
}
