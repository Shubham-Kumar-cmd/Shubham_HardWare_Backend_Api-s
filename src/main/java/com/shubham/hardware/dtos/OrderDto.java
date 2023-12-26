package com.shubham.hardware.dtos;

import com.shubham.hardware.entities.OrderItem;
import com.shubham.hardware.entities.User;
import com.shubham.hardware.enums.OrderStatus;
import com.shubham.hardware.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDto {
    private String orderId;

    //    PENDING,DISPATCHED,DELIVERED
    private OrderStatus orderStatus=OrderStatus.PENDING;

    //    PAID,NOTPAID
    private PaymentStatus paymentStatus=PaymentStatus.NOTPAID;

    private double orderAmount;
    private String billingAddress;
    private String billingPhone;
    private String billingName;
    private Date orderedDate=new Date();
    private Date deliveredDate;
//    private UserDto user;
    private List<OrderItemDto> orderItems=new ArrayList<>();

    private String razorPayOrderId;

    private String paymentId;

}
