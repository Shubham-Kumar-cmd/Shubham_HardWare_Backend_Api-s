package com.shubham.hardware.services.impl;

import com.shubham.hardware.dtos.AddItemToCartRequest;
import com.shubham.hardware.dtos.CartDto;
import com.shubham.hardware.dtos.CartItemDto;
import com.shubham.hardware.entities.Cart;
import com.shubham.hardware.entities.CartItem;
import com.shubham.hardware.entities.Product;
import com.shubham.hardware.entities.User;
import com.shubham.hardware.exceptions.BadApiRequestException;
import com.shubham.hardware.exceptions.ResourceNotFoundException;
import com.shubham.hardware.repo.CartItemRepository;
import com.shubham.hardware.repo.CartRepository;
import com.shubham.hardware.repo.ProductRepository;
import com.shubham.hardware.repo.UserRepository;
import com.shubham.hardware.services.CartService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ModelMapper modelMapper;

    private Logger logger= LoggerFactory.getLogger(CartServiceImpl.class);

    //add items to cart
//    java.sql.SQLIntegrityConstraintViolationException: Duplicate entry '6bcd3009-553f-47a4-86bc-065240ada69f' for key 'cart_items.UK_stjbkkh1eftd036p0v219glxw'
    @Override
    public CartDto addItemToCart(String userId, AddItemToCartRequest addItemToCartRequest) {

        String productId = addItemToCartRequest.getProductId();
        int quantity = addItemToCartRequest.getQuantity();

        if (quantity<=0){
            throw new BadApiRequestException("Requested quantity is not valid!!");
        }

//        fetch the product
        Product product = productRepository.findById(productId).orElseThrow(()->new ResourceNotFoundException("Product not found!!"));

//        fetch the user
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found!!"));

//        fetch the cart of corresponding user
        Cart cart = null;
        try {
//    case2: cart is available : then add the items to the cart
            cart = cartRepository.findByUser(user).get();
            logger.info("Cart of current user : {}",cart);
        }catch (NoSuchElementException e){
//    case1: cart for user is not available: then we will create a cart then add the items to cart
            cart = new Cart();

//            generating cartId
            String cartId= UUID.randomUUID().toString();
            cart.setCartId(cartId);

//            generating cart creation date
            cart.setCreatedAt(new Date());
            logger.info("Cart not found with given userId : {}",e.getMessage());
        }

//        perform cart operation
//        check if the items already present  : then update accordingly
        AtomicBoolean updated = new AtomicBoolean(false);
        List<CartItem> cartItems = cart.getCartItems();
//        List<CartItem> updatedItems = cartItems.stream().map(item -> {
        cartItems = cartItems.stream().map(item -> {
            if (item.getProduct().getProductId().equals(productId)) {
//                item already in cart
//                so we have to increase by one
                item.setQuantity(quantity);
                item.setTotalPrice((long) quantity * product.getDiscountedPrice());
                updated.set(true);
            }
            return item;
        }).toList();
        logger.info("CartItems : {}",cartItems);

//        cart.setCartItems(updatedItems);

//        create items
//        updated.get() == true
        if (!updated.get()){
            CartItem cartItem = CartItem.builder()
                    .quantity(quantity)
                    .totalPrice((long) quantity * product.getDiscountedPrice())
                    .cart(cart)
                    .product(product)
                    .build();
            cart.getCartItems().add(cartItem);
        }


        cart.setUser(user);
        logger.info("Current User : {}",user);
        logger.info("Current Product : {}",product);
        Cart cartUpdated = cartRepository.save(cart);
        return modelMapper.map(cartUpdated, CartDto.class);
    }

    //    remove item from cart
    @Override
    public void removeItemFromCart(String userId, int cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(() -> new ResourceNotFoundException("Cart item not found!!"));
        cartItemRepository.delete(cartItem);
    }

    //    remove all items from cart
    @Override
    public void clearCart(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with given id!!"));
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("Cart not found!!"));
        cart.getCartItems().clear();
        logger.info("Cart is empty now : {}",cart);
        cartRepository.save(cart);
    }

    @Override
    public CartDto getCartByUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with given id!!"));
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("Cart not found!!"));
        return modelMapper.map(cart, CartDto.class);
    }
}
