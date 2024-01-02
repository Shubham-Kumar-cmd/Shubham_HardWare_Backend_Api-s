package com.shubham.hardware.services.impl;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.shubham.hardware.enums.OrderStatus;
import com.shubham.hardware.enums.PaymentStatus;
import com.shubham.hardware.exceptions.ResourceNotFoundException;
import com.shubham.hardware.repo.OrderRepository;
import com.shubham.hardware.services.PaymentService;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private OrderRepository orderRepository;

    private Order orderCreated=null;

    @Value(value = "${razorpay.payment.key}")
    private String KEY;

    @Value(value = "${razorpay.payment.secret-key}")
    private String KEY_SECRET;

    private Logger logger= LoggerFactory.getLogger(PaymentServiceImpl.class);

    @Override
    public Order createPaymentRequest(Map<String, Object> data,String orderId) {
        logger.info("Data :{}",data);
        int amount=Integer.parseInt(data.get("amount").toString());
        logger.info("Amount : {}",amount);
        try {
            //RazorpayClient razorpayClient = new RazorpayClient("rzp_test_haDRsjIQo9vFPJ", "owKJJes2fwE6YD6DToishFuH");
            var razorpayClient=new RazorpayClient(KEY, KEY_SECRET);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("amount", amount*100);// amount in the smallest currency unit(here, amount is in paisa not rupee)
            jsonObject.put("currency", "INR");

            //creating new order
            orderCreated = razorpayClient.orders.create(jsonObject);
            logger.info("Order created!! : {}",orderCreated);

            //here we can perform save operation to save the detail of order
            com.shubham.hardware.entities.Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order not exist with given order id!!"));
            order.setRazorPayOrderId(orderCreated.get("id"));
            order.setOrderStatus(OrderStatus.PLACED);
//            order.setOrderAmount((Double) orderCreated.get("amount"));
//            order.setPaymentId(null);
            this.orderRepository.save(order);
        } catch (RazorpayException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }catch(Exception e) {
            e.printStackTrace();
        }
        return orderCreated;
    }

    @Override
    public String saveDataToDatabase(Map<String, Object> data) {
//        razor_pay_order_id of database is equal to order_id of RazorPay
        com.shubham.hardware.entities.Order myOrder = this.orderRepository.findByRazorPayOrderId(data.get("order_id").toString());
        String status = data.get("status").toString();
        logger.info("Order Status : {}",status);
        myOrder.setPaymentId(data.get("payment_id").toString());
        myOrder.setPaymentStatus(PaymentStatus.PAID);
        this.orderRepository.save(myOrder);
        return "Updated";
    }
}
