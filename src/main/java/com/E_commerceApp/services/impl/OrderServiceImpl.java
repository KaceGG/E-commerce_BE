package com.E_commerceApp.services.impl;

import com.E_commerceApp.DTOs.response.ApiResponse;
import com.E_commerceApp.DTOs.response.OrderResponse;
import com.E_commerceApp.exception.AppException;
import com.E_commerceApp.exception.ErrorCode;
import com.E_commerceApp.mappers.OrderMapper;
import com.E_commerceApp.models.Order;
import com.E_commerceApp.models.User;
import com.E_commerceApp.repositories.OrderRepository;
import com.E_commerceApp.repositories.UserRepository;
import com.E_commerceApp.services.OrderService;
import com.E_commerceApp.services.ZaloPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private ZaloPayService zaloPayService;

    @Override
    public ApiResponse<List<OrderResponse>> getAllOrders() {
        ApiResponse<List<OrderResponse>> apiResponse = new ApiResponse<>();

        List<Order> orders = orderRepository.findAll();
        if (orders.isEmpty()) {
            apiResponse.setMessage("No orders found");
            apiResponse.setResult(null);
        } else {
            List<OrderResponse> orderResponses = orders.stream()
                    .map(orderMapper::toOrderResponse)
                    .collect(Collectors.toList());
            apiResponse.setMessage("All orders retrieved successfully");
            apiResponse.setResult(orderResponses);
        }
        return apiResponse;
    }

    public ApiResponse<List<OrderResponse>> getOrdersByUserId(String userId) {
        ApiResponse<List<OrderResponse>> apiResponse = new ApiResponse<>();

        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty())
            throw new AppException(ErrorCode.USER_NOT_FOUND);

        List<Order> orders = orderRepository.findByUserId(userId);
        if (orders.isEmpty()) {
            apiResponse.setMessage("No orders found for user: " + userId);
            apiResponse.setResult(null);
        } else {
            List<OrderResponse> orderResponses = orders.stream()
                    .map(orderMapper::toOrderResponse)
                    .collect(Collectors.toList());
            apiResponse.setMessage("Orders retrieved successfully");
            apiResponse.setResult(orderResponses);
        }
        return apiResponse;
    }

    @Override
    public Void cancelOrder(int orderId, String userId) {
        return null;
    }

//    @Override
//    public OrderResponse cancelOrder(RefundRequest request) throws Exception {
//        // Tìm order theo ID
//        Order order = orderRepository.findById(request.getOrderId())
//                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + request.getOrderId()));
//
//        // Kiểm tra trạng thái hiện tại
//        if (order.getStatus() == OrderStatus.CANCELED) {
//            throw new RuntimeException("Order is already canceled");
//        }
//
//        // Gọi ZaloPay refund API
//        Map<String, Object> refundResult = zaloPayService.refundOrder(
//                request.getZpTransId(),
//                request.getAmount(),
//                request.getReason()
//        );
//
//        // Kiểm tra kết quả refund
//        int returnCode = (int) refundResult.get("return_code");
//        if (returnCode == 1) { // Refund thành công
//            order.setStatus(OrderStatus.CANCELED);
//            orderRepository.save(order);
//        } else {
//            throw new RuntimeException("Refund failed: " + refundResult.get("return_message"));
//        }
//
//        return orderMapper.toOrderResponse(order);
//    }
}
