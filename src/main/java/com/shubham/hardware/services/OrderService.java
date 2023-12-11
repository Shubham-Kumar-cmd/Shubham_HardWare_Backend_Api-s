package com.shubham.hardware.services;

import com.shubham.hardware.dtos.CreateOrderRequest;
import com.shubham.hardware.dtos.OrderDto;
import com.shubham.hardware.dtos.PageableResponse;
import com.shubham.hardware.dtos.UpdatePaymentAndOrderRequest;

import java.util.List;

public interface OrderService {

//    create order
    OrderDto createOrder(CreateOrderRequest createOrderRequest);

//    update order
    OrderDto updateOrder(String orderId, UpdatePaymentAndOrderRequest updateOrderRequest);

//    remove order
    void removeOrder(String orderId);

//    get orders of user
    List<OrderDto> getOrdersOfUser(String userId);

//    get orders
    PageableResponse<OrderDto> getOrders(int pageNumber,int pageSize,String sortBy,String sortDir);


}
