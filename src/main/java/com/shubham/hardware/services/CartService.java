package com.shubham.hardware.services;

import com.shubham.hardware.dtos.AddItemToCartRequest;
import com.shubham.hardware.dtos.CartDto;

public interface CartService {

    //add items to cart
//    case1: cart for user is not available: then we will create a cart then add the items to cart
//    case2: cart is available : then add the items to cart

    CartDto addItemToCart(String userId, AddItemToCartRequest addItemToCartRequest);

//    remove item from cart
    void removeItemFromCart(String userId,int cartItemId);

//    remove all items from cart
    void clearCart(String userId);

//    fetch cart of user
    CartDto getCartByUser(String userId);
}
