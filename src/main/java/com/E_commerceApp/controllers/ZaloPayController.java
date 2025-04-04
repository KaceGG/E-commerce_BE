package com.E_commerceApp.controllers;

import com.E_commerceApp.DTOs.request.OrderRequest;
import com.E_commerceApp.DTOs.response.ApiResponse;
import com.E_commerceApp.DTOs.response.ZaloPayResponseDTO;
import com.E_commerceApp.exception.AppException;
import com.E_commerceApp.exception.ErrorCode;
import com.E_commerceApp.mappers.OrderMapper;
import com.E_commerceApp.models.*;
import com.E_commerceApp.repositories.OrderRepository;
import com.E_commerceApp.repositories.PaymentRepository;
import com.E_commerceApp.repositories.ProductRepository;
import com.E_commerceApp.repositories.UserRepository;
import com.E_commerceApp.services.CartService;
import com.E_commerceApp.services.ZaloPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/payment/zalopay")
public class ZaloPayController {

    @Autowired
    private ZaloPayService zaloPayService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartService cartService;

    @PostMapping("/create-order")
    public ApiResponse<Map<String, Object>> createOrder(
            @RequestBody OrderRequest request) throws Exception {
        ApiResponse<Map<String, Object>> apiResponse = new ApiResponse<>();

        // Kiểm tra dữ liệu đầu vào
        if (request == null || request.getUserId() == null || request.getAmount() <= 0) {
            apiResponse.setMessage("Invalid request data");
            apiResponse.setResult(null);
            return apiResponse;
        }

        // Tìm User dựa trên userId
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + request.getUserId()));

        // Tạo Order trước khi gọi ZaloPay
        Order order = new Order();
        order.setOrderDate(LocalDateTime.now());
        order.setTotalAmount(request.getAmount());
        order.setStatus(OrderStatus.PENDING);
        order.setUser(user);
        order.setShippingAddress(request.getShippingAddress() != null ? request.getShippingAddress() : "Default Address");

        // Ánh xạ và lưu OrderItem với Product
        List<OrderItem> orderItems = orderMapper.toOrderItems(request.getItems());
        for (int i = 0; i < orderItems.size(); i++) {
            OrderItem item = orderItems.get(i);
            item.setOrder(order);
            int productId = request.getItems().get(i).getProductId();
            if (productId <= 0) {
                throw new IllegalArgumentException("Invalid productId at index " + i);
            }
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
            item.setProduct(product);
        }
        order.setItems(orderItems);

        // Lưu Order vào DB
        order = orderRepository.save(order);

        // Gọi ZaloPay để tạo thanh toán
        ZaloPayResponseDTO zaloResponse = zaloPayService.createOrder(request);

        // Tạo Payment và liên kết với Order
        Payment payment = new Payment();
        payment.setPaymentMethod(PaymentMethod.ZALOPAY);
        payment.setOrder(order);
        payment.setOrderToken(zaloResponse.getZpTransToken());
        payment.setPaymentUrl(zaloResponse.getOrderUrl());
        payment.setAmount(zaloResponse.getAmount());
        payment.setPaymentDate(LocalDateTime.now());
        payment.setStatus(zaloResponse
                .getReturnCode() == 1 ? OrderStatus.SUCCESS : OrderStatus.PENDING);

        // Cập nhật trạng thái Order dựa trên phản hồi ZaloPay
        if (zaloResponse.getReturnCode() == 1) {
            order.setStatus(OrderStatus.SUCCESS);

            // Clear giỏ hàng khi thanh toán thành công
            cartService.clearCart(request.getUserId());
            System.out.println("Cart cleared for user ID: " + request.getUserId());
        }
        order.setPayment(payment);

        System.out.println("Zalo response amount: "  + zaloResponse.getAmount());
        System.out.println("Order status: "  + order.getStatus());

        // Lưu Payment và cập nhật Order
        paymentRepository.save(payment);
        orderRepository.save(order);

        // Chuẩn bị phản hồi cho client
        Map<String, Object> result = new HashMap<>();
        result.put("orderUrl", zaloResponse.getOrderUrl());
        result.put("orderToken", zaloResponse.getOrderToken());
        result.put("zpTransToken", zaloResponse.getZpTransToken());
        result.put("returnCode", zaloResponse.getReturnCode());
        result.put("returnMessage", zaloResponse.getReturnMessage());
        apiResponse.setMessage("Order created successfully");
        apiResponse.setResult(result);

        return apiResponse;
    }
}