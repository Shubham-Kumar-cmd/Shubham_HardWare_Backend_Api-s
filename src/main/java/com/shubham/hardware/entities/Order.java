package com.shubham.hardware.entities;

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
//@ToString
@Entity
@Table(name = "orders")
public class Order {

    @Id
    private String orderId;

//    PENDING,DISPATCHED,DELIVERED
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;


//    PAID,NOTPAID
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    private double orderAmount;

    @Column(length = 1000)
    private String billingAddress;

    private String billingPhone;
    private String billingName;
    private Date orderedDate;
    private Date deliveredDate;

//    User
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private List<OrderItem> orderItems=new ArrayList<>();

    private String razorPayOrderId;

    private String paymentId;
}
