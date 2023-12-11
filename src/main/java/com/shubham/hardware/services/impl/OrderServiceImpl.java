package com.shubham.hardware.services.impl;

import com.shubham.hardware.dtos.CreateOrderRequest;
import com.shubham.hardware.dtos.OrderDto;
import com.shubham.hardware.dtos.PageableResponse;
import com.shubham.hardware.dtos.UpdatePaymentAndOrderRequest;
import com.shubham.hardware.entities.*;
import com.shubham.hardware.exceptions.BadApiRequestException;
import com.shubham.hardware.exceptions.ResourceNotFoundException;
import com.shubham.hardware.helper.Helper;
import com.shubham.hardware.repo.CartRepository;
import com.shubham.hardware.repo.OrderRepository;
import com.shubham.hardware.repo.UserRepository;
import com.shubham.hardware.services.OrderService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ModelMapper modelMapper;

    private Logger logger= LoggerFactory.getLogger(OrderServiceImpl.class);

    @Override
    public OrderDto createOrder(CreateOrderRequest createOrderRequest) {

        String userId = createOrderRequest.getUserId();

        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with given id!!"));
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("Cart not found of given user!!"));
        List<CartItem> cartItems = cart.getCartItems();

//        checks
        if (cartItems.size() == 0){
            throw new BadApiRequestException("Empty cart!!");
        }
        Order order = Order.builder()
                .orderId(UUID.randomUUID().toString())
                .orderStatus(createOrderRequest.getOrderStatus())
                .paymentStatus(createOrderRequest.getPaymentStatus())
                .billingAddress(createOrderRequest.getBillingAddress())
                .billingName(createOrderRequest.getBillingName())
                .billingPhone(createOrderRequest.getBillingPhone())
                .orderedDate(new Date())
//                .deliveredDate(createOrderRequest.getDeliveredDate())
                .user(user)
                .build();
//        orderItems,orderAmount
        AtomicReference<Double> orderAmount = new AtomicReference<>(0.0);
        List<OrderItem> orderItems = cartItems.stream().map(cartItem -> {

//            convert cartItem->orderItem
            OrderItem orderItem = OrderItem.builder()
                    .quantity(cartItem.getQuantity())
                    .product(cartItem.getProduct())
                    .totalPrice(cartItem.getQuantity() * cartItem.getProduct().getDiscountedPrice())
                    .order(order)
                    .build();
            orderAmount.set(orderAmount.get()+ orderItem.getTotalPrice());
            return orderItem;
        }).toList();

//        setting orderItems and total amount of order
        order.setOrderItems(orderItems);
        order.setOrderAmount(orderAmount.get());

//        clear the cart just before placing the order
        cart.getCartItems().clear();

        cartRepository.save(cart);
        Order savedOrder = orderRepository.save(order);

        return modelMapper.map(savedOrder, OrderDto.class);
    }

    @Override
    public OrderDto updateOrder(String orderId, UpdatePaymentAndOrderRequest updateOrderRequest) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order not found!!"));
        order.setDeliveredDate(new Date());
        order.setOrderStatus(updateOrderRequest.getOrderStatus());
        order.setPaymentStatus(updateOrderRequest.getPaymentStatus());
        Order orderUpdated = orderRepository.save(order);
        logger.info("Order delivered date : {}",order.getDeliveredDate());
        return modelMapper.map(orderUpdated,OrderDto.class);
    }

    @Override
    public void removeOrder(String orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order not found!!"));
        orderRepository.delete(order);
    }

    @Override
    public List<OrderDto> getOrdersOfUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with given id!!"));
        List<Order> orders = orderRepository.findByUser(user);
        List<OrderDto> orderDtoList = orders.stream().map(order -> modelMapper.map(order, OrderDto.class)).toList();
        return orderDtoList;
    }

    @Override
    public PageableResponse<OrderDto> getOrders(int pageNumber, int pageSize, String sortBy, String sortDir) {

        Sort sort=(sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()) :(Sort.by(sortBy).ascending()) ;
        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);

        Page<Order> page = orderRepository.findAll(pageable);
        PageableResponse<OrderDto> response = Helper.getPageableResponse(page, OrderDto.class);
        return response;
    }
}
