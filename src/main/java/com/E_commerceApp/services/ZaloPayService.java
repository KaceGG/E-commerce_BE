package com.E_commerceApp.services;

import com.E_commerceApp.DTOs.request.OrderRequest;
import com.E_commerceApp.DTOs.response.ZaloPayResponseDTO;
import com.E_commerceApp.utils.HMACUtil;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ZaloPayService {

    public static final String NGROK_URL =
            "https://488f-2402-800-6343-74f1-88bc-3245-9302-5530.ngrok-free.app";

    private static final Map<String, String> config = new HashMap<>() {{
        put("app_id", "2554");
        put("key1", "sdngKKJmqEMzvh5QQcdD2A9XBSKUNaYn");
        put("key2", "trMrHtvjo6myautxDUiAcYsVtaeQ8nhf");
        put("endpoint", "https://sb-openapi.zalopay.vn/v2/create");
    }};

    public ZaloPayResponseDTO createOrder(OrderRequest request) throws Exception {
        Random rand = new Random();
        int randomId = rand.nextInt(1000000);
        String appTransId = getCurrentTimeString() + "_" + randomId;

        Map<String, Object> order = new HashMap<>();
        order.put("app_id", config.get("app_id"));
        order.put("app_trans_id", appTransId);
        order.put("app_time", System.currentTimeMillis());
        order.put("app_user", request.getUserId());
        order.put("amount", request.getAmount());
        order.put("description", " ");
        order.put("bank_code", "zalopayapp");
        order.put("item", new JSONArray(request.getItems()).toString());
        order.put("embed_data", new JSONObject(new HashMap<>()).toString());
        order.put("callback_url", NGROK_URL + "/callback");

        String data = order.get("app_id") + "|" + order.get("app_trans_id") + "|" +
                order.get("app_user") + "|" + order.get("amount") + "|" +
                order.get("app_time") + "|" + order.get("embed_data") + "|" +
                order.get("item");
        order.put("mac", HMACUtil.HMacHexStringEncode(
                HMACUtil.HMACSHA256, config.get("key1"), data));

        System.out.println("Order data: " + order);
        System.out.println("Order data item: " + order.get("item"));

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost post = new HttpPost(config.get("endpoint"));
            List<NameValuePair> params = new ArrayList<>();
            for (Map.Entry<String, Object> e : order.entrySet()) {
                params.add(new BasicNameValuePair(e.getKey(), e.getValue().toString()));
            }
            post.setEntity(new UrlEncodedFormEntity(params));

            try (CloseableHttpResponse res = client.execute(post)) {
                BufferedReader rd = new BufferedReader(new InputStreamReader(res.getEntity().getContent()));
                StringBuilder resultJsonStr = new StringBuilder();
                String line;
                while ((line = rd.readLine()) != null) {
                    resultJsonStr.append(line);
                }
                JSONObject jsonResponse = new JSONObject(resultJsonStr.toString());
                System.out.println("Response: " + jsonResponse);

                ZaloPayResponseDTO responseDTO = new ZaloPayResponseDTO();
                responseDTO.setOrderUrl(jsonResponse.getString("order_url"));
                responseDTO.setOrderToken(jsonResponse.getString("order_token"));
                responseDTO.setZpTransToken(jsonResponse.getString("zp_trans_token"));
                responseDTO.setReturnCode(jsonResponse.getInt("return_code"));
                responseDTO.setReturnMessage(jsonResponse.getString("return_message"));
                responseDTO.setAmount(request.getAmount());
                responseDTO.setAppTransId(appTransId);

                return responseDTO;
            }
        }
    }

    public Map<String, Object> refundOrder(String zpTransId, double amount, String description) throws Exception {
        Random rand = new Random();
        long timestamp = System.currentTimeMillis();
        String uid = timestamp + "" + (111 + rand.nextInt(888));
        String mRefundId = getCurrentTimeString() + "_" + config.get("app_id") + "_" + uid;

        // Tạo đối tượng order với thông tin refund
        Map<String, Object> order = new HashMap<>();
        order.put("app_id", config.get("app_id"));
        order.put("zp_trans_id", zpTransId); // Mã giao dịch ZaloPay cần hoàn tiền
        order.put("m_refund_id", mRefundId); // ID refund unique
        order.put("timestamp", timestamp);
        order.put("amount", amount); // Số tiền cần hoàn
        order.put("description", description.isEmpty() ? "Refund for order" : description);

        // Tạo MAC
        String data = order.get("app_id") + "|" + order.get("zp_trans_id") + "|" +
                order.get("amount") + "|" + order.get("description") + "|" +
                order.get("timestamp");
        order.put("mac", HMACUtil.HMacHexStringEncode(HMACUtil.HMACSHA256, config.get("key1"), data));

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost post = new HttpPost("https://sb-openapi.zalopay.vn/v2/refund");
            List<NameValuePair> params = new ArrayList<>();
            for (Map.Entry<String, Object> e : order.entrySet()) {
                if (!e.getKey().equals("status")) {
                    params.add(new BasicNameValuePair(e.getKey(), e.getValue().toString()));
                }
            }
            post.setEntity(new UrlEncodedFormEntity(params));

            try (CloseableHttpResponse res = client.execute(post)) {
                BufferedReader rd = new BufferedReader(new InputStreamReader(res.getEntity().getContent()));
                StringBuilder resultJsonStr = new StringBuilder();
                String line;
                while ((line = rd.readLine()) != null) {
                    resultJsonStr.append(line);
                }
                JSONObject jsonResponse = new JSONObject(resultJsonStr.toString());
                System.out.println(jsonResponse);

                // Cập nhật status dựa trên response
                int returnCode = jsonResponse.getInt("return_code");
                if (returnCode == 1) {
                    order.put("status", "CANCELED");
                } else {
                    order.put("status", "FAILED");
                }

                // Thêm thông tin response vào order
                order.put("return_code", returnCode);
                order.put("return_message", jsonResponse.getString("return_message"));

                return order;
            }
        }
    }

    private String getCurrentTimeString() {
        Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("GMT+7"));
        SimpleDateFormat fmt = new SimpleDateFormat("yyMMdd");
        fmt.setCalendar(cal);
        return fmt.format(cal.getTimeInMillis());
    }
}