package com.shubham.hardware.controllers;

import com.shubham.hardware.dtos.AddItemToCartRequest;
import com.shubham.hardware.dtos.ApiResponseMessage;
import com.shubham.hardware.dtos.CartDto;
import com.shubham.hardware.services.CartService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/shubham-hardware/carts")
@Tag(name = "CartController",description = "REST APIS related to perform cart operations!!")
@SecurityRequirement(name = "bearerScheme")
public class CartControllers {

    @Autowired
    private CartService cartService;

    private Logger logger= LoggerFactory.getLogger(CartControllers.class);

//    add items to cart
    @PostMapping("/{userId}")
    public ResponseEntity<CartDto> addItemsToCart(@RequestBody AddItemToCartRequest addItemToCartRequest, @PathVariable("userId") String userid){
        CartDto cartDto = cartService.addItemToCart(userid, addItemToCartRequest);
        return new ResponseEntity<>(cartDto, HttpStatus.OK);
    }

//    remove item from cart
    @DeleteMapping("/{userId}/item/{cartItemId}")
    public ResponseEntity<ApiResponseMessage> removeItemFromCart(
            @PathVariable("userId") String userId,
            @PathVariable("cartItemId") int cartItemId
    ){
        cartService.removeItemFromCart(userId,cartItemId);
        ApiResponseMessage responseMessage = ApiResponseMessage.builder()
                .message("Item removed from cart!!")
                .success(true)
                .status(HttpStatus.OK)
                .build();
        return new ResponseEntity<>(responseMessage,HttpStatus.OK);
    }

//    clear cart
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponseMessage> clearCart(
            @PathVariable("userId") String userId
    ){
        cartService.clearCart(userId);
        ApiResponseMessage responseMessage = ApiResponseMessage.builder()
                .message("empty cart!!")
                .success(true)
                .status(HttpStatus.OK)
                .build();
        return new ResponseEntity<>(responseMessage,HttpStatus.OK);
    }

//    get cart of user
    @GetMapping("/{userId}")
    public ResponseEntity<CartDto> getCartOfUser(@PathVariable("userId") String userId){
        CartDto cartDto = cartService.getCartByUser(userId);
        return new ResponseEntity<>(cartDto,HttpStatus.OK);
    }
}
