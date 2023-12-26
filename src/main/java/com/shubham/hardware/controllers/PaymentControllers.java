package com.shubham.hardware.controllers;

import com.razorpay.Order;
import com.shubham.hardware.dtos.OrderDto;
import com.shubham.hardware.services.PaymentService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/shubham-hardware")
@Tag(name = "PaymentController",description = "REST APIS related to perform payment operations!!")
@SecurityRequirement(name = "bearerScheme")
public class PaymentControllers {

    @Autowired
    private PaymentService paymentService;

    private Logger logger= LoggerFactory.getLogger(PaymentControllers.class);

    @PostMapping("/payment/create/{orderId}")
    private ResponseEntity<Order> createOrderRequest(@RequestBody Map<String, Object> data,@PathVariable("orderId") String orderId){
        Order createPaymentRequest = paymentService.createPaymentRequest(data,orderId);
        logger.info("Order : {}",createPaymentRequest);
//        OrderDto orderDto=OrderDto.builder().build();
        return new ResponseEntity<>(createPaymentRequest, HttpStatus.CREATED);
    }
}
