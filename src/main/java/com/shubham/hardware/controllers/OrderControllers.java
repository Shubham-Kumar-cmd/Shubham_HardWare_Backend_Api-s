package com.shubham.hardware.controllers;

import com.shubham.hardware.dtos.*;
import com.shubham.hardware.services.OrderService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shubham-hardware/orders")
@Tag(name = "OrderController",description = "REST APIS related to perform order operations!!")
public class OrderControllers {

    @Autowired
    private OrderService orderService;

    private Logger logger= LoggerFactory.getLogger(OrderControllers.class);

    //create
    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@Valid @RequestBody CreateOrderRequest createOrderRequest){
        OrderDto order = orderService.createOrder(createOrderRequest);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

//    update order
    @PutMapping("/update/{orderId}")
    public ResponseEntity<OrderDto> updateOrder(@RequestBody UpdatePaymentAndOrderRequest updatePaymentAndOrderRequest,@PathVariable("orderId") String orderId){
        OrderDto orderDto = orderService.updateOrder(orderId, updatePaymentAndOrderRequest);
        return new ResponseEntity<>(orderDto,HttpStatus.OK);
    }

//    remove order
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{orderId}")
    public ResponseEntity<ApiResponseMessage> removeOrder(@PathVariable("orderId") String orderId){
        orderService.removeOrder(orderId);
        ApiResponseMessage responseMessage = ApiResponseMessage.builder()
                .message("Order deleted successfully!!")
                .success(true)
                .status(HttpStatus.OK)
                .build();
        return new ResponseEntity<>(responseMessage,HttpStatus.OK);
    }

//    get orders of User
    @GetMapping("/users/{userId}")
    public ResponseEntity<List<OrderDto>> getOrdersOfUser(@PathVariable("userId") String userId){
        List<OrderDto> orders = orderService.getOrdersOfUser(userId);
        return new ResponseEntity<>(orders,HttpStatus.OK);
    }

//    get all orders
    @GetMapping
    public ResponseEntity<PageableResponse<OrderDto>> getAllOrders(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "orderedDate", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ){
        PageableResponse<OrderDto> orders = orderService.getOrders(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(orders,HttpStatus.OK);
    }

}
