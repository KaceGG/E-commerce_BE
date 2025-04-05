package com.E_commerceApp.controllers;

import com.E_commerceApp.models.Order;
import com.E_commerceApp.models.OrderStatus;
import com.E_commerceApp.models.Payment;
import com.E_commerceApp.repositories.OrderRepository;
import com.E_commerceApp.repositories.PaymentRepository;
import com.E_commerceApp.services.CartService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.util.logging.Logger;

@RestController
@CrossOrigin("*")
public class CallBackController {

    private Logger logger = Logger.getLogger(this.getClass().getName());
    private String key2 = "trMrHtvjo6myautxDUiAcYsVtaeQ8nhf";
    private Mac HmacSHA256;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private CartService cartService;

    public CallBackController() throws Exception {
        HmacSHA256 = Mac.getInstance("HmacSHA256");
        HmacSHA256.init(new SecretKeySpec(key2.getBytes(), "HmacSHA256"));
    }

    @PostMapping("/callback")
    public String callback(@RequestBody String jsonStr) {
        JSONObject result = new JSONObject();

        try {
            JSONObject cbdata = new JSONObject(jsonStr);
            String dataStr = cbdata.getString("data");
            String reqMac = cbdata.getString("mac");

            byte[] hashBytes = HmacSHA256.doFinal(dataStr.getBytes());
            String mac = DatatypeConverter.printHexBinary(hashBytes).toLowerCase();

            // kiểm tra callback hợp lệ (đến từ ZaloPay server)
            if (!reqMac.equals(mac)) {
                // callback không hợp lệ
                result.put("return_code", -1);
                result.put("return_message", "mac not equal");
            } else {
                // Thanh toán thành công
                JSONObject data = new JSONObject(dataStr);
                String appTransId = data.getString("app_trans_id");
                System.out.println("Received callback for app_trans_id: " + appTransId);

                // Tìm Payment dựa trên app_trans_id (orderToken)
                Payment payment = paymentRepository.findByOrderToken(appTransId)
                        .orElseThrow(() -> new IllegalStateException("Payment not found for app_trans_id: " + appTransId));

                // Tìm Order liên quan
                Order order = payment.getOrder();
                if (order == null) {
                    throw new IllegalStateException("Order not found for payment with app_trans_id: " + appTransId);
                }

                // Cập nhật trạng thái Order và Payment thành SUCCESS
                order.setStatus(OrderStatus.SUCCESS);
                payment.setStatus(OrderStatus.SUCCESS);

                // Clear giỏ hàng của người dùng
                String userId = order.getUser().getId();
                cartService.clearCart(userId);
                System.out.println("Cart cleared for user ID: " + userId);

                // Lưu thay đổi
                paymentRepository.save(payment);
                orderRepository.save(order);

                logger.info("Updated order status to SUCCESS for app_trans_id: " + appTransId);
                logger.info("Found payment for app_trans_id: " + appTransId);
                logger.info("Found order with ID: " + order.getId());
                logger.info("Updated order status to SUCCESS for app_trans_id: " + appTransId);

                result.put("return_code", 1);
                result.put("return_message", "success");
            }
        } catch (Exception ex) {
            result.put("return_code", 0); // ZaloPay server sẽ callback lại (tối đa 3 lần)
            result.put("return_message", ex.getMessage());
        }

        // thông báo kết quả cho ZaloPay server
        return result.toString();
    }
}
