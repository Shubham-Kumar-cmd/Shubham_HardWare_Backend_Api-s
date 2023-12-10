package com.shubham.hardware.dtos;

import com.shubham.hardware.entities.CartItem;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartDto {

    private String cartId;
    private Date createdAt;
    private UserDto user;

    private List<CartItemDto> cartItems=new ArrayList<>();
}
