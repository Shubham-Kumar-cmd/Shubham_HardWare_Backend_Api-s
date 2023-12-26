package com.shubham.hardware.services;

import com.razorpay.Order;

import java.util.Map;

public interface PaymentService {
    Order createPaymentRequest(Map<String, Object> data,String orderId);
    String saveDataToDatabase(Map<String, Object> data);
}
