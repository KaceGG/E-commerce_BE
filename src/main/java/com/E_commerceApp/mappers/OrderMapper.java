package com.E_commerceApp.mappers;

import com.E_commerceApp.DTOs.request.OrderItemRequest;
import com.E_commerceApp.DTOs.request.OrderRequest;
import com.E_commerceApp.DTOs.response.OrderItemResponse;
import com.E_commerceApp.DTOs.response.OrderResponse;
import com.E_commerceApp.DTOs.response.PaymentResponse;
import com.E_commerceApp.models.Order;
import com.E_commerceApp.models.OrderItem;
import com.E_commerceApp.models.Payment;
import com.E_commerceApp.models.PaymentMethod;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orderDate", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "totalAmount", expression = "java(calculateTotalAmount(request.getItems()))")
    @Mapping(target = "user", ignore = true) // Sẽ được set trong service
    @Mapping(target = "items", source = "items")
    @Mapping(target = "payment", ignore = true)
        // Sẽ được set trong service
    Order toOrder(OrderRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "order", ignore = true) // Sẽ được set trong service
    @Mapping(target = "product", ignore = true)
        // Sẽ được set trong service
    OrderItem toOrderItem(OrderItemRequest request);

    List<OrderItem> toOrderItems(List<OrderItemRequest> requests);

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "items", target = "items")
    @Mapping(source = "payment", target = "payment")
    OrderResponse toOrderResponse(Order order);

    @Mapping(source = "product.id", target = "productId")
    OrderItemResponse toOrderItemResponse(OrderItem orderItem);

    List<OrderItemResponse> toOrderItemResponses(List<OrderItem> orderItems);

    @Mapping(source = "paymentMethod", target = "paymentMethod",
            qualifiedByName = "mapPaymentMethodToString")
    PaymentResponse toPaymentResponse(Payment payment);

    @Named("mapPaymentMethodToString")
    default String mapPaymentMethodToString(PaymentMethod paymentMethod) {
        return paymentMethod != null ? paymentMethod.toString() : null;
    }

    default float calculateTotalAmount(List<OrderItemRequest> items) {
        if (items == null || items.isEmpty()) {
            return 0.0f;
        }
        return (float) items.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
    }
}
