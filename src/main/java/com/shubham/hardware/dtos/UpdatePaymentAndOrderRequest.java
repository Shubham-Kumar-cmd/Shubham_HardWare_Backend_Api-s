package com.shubham.hardware.dtos;

import com.shubham.hardware.enums.OrderStatus;
import com.shubham.hardware.enums.PaymentStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdatePaymentAndOrderRequest {

    //    PENDING,DISPATCHED,DELIVERED
    private OrderStatus orderStatus;//=OrderStatus.PENDING;

    //    PAID,NOTPAID
    private PaymentStatus paymentStatus;//=PaymentStatus.PAID;



}
