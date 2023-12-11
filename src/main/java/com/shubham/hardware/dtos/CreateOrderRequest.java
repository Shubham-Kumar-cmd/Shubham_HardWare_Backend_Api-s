package com.shubham.hardware.dtos;

import com.shubham.hardware.enums.OrderStatus;
import com.shubham.hardware.enums.PaymentStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class CreateOrderRequest {

    @NotBlank(message = "CartId is required!!")
    private String userId;

    //    PENDING,DISPATCHED,DELIVERED
    private OrderStatus orderStatus=OrderStatus.PENDING;

    //    PAID,NOTPAID
    private PaymentStatus paymentStatus=PaymentStatus.NOTPAID;

    @NotBlank(message = "Address is required!!")
    @Size(min = 15,message = "full address is required!!")
    private String billingAddress;

    @NotBlank(message = "Mobile number required!!")
    @Size(min = 10,message = "Mobile no. requires 10 digit")
    private String billingPhone;

    @NotBlank(message = "Billing name required!!")
    private String billingName;
}
