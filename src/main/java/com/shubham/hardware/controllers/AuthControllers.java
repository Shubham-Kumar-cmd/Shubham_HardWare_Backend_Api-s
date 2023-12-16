package com.shubham.hardware.controllers;

import com.shubham.hardware.dtos.JwtRequest;
import com.shubham.hardware.dtos.JwtResponse;
import com.shubham.hardware.dtos.UserDto;
import com.shubham.hardware.exceptions.BadApiRequestException;
import com.shubham.hardware.security.JwtHelper;
import com.shubham.hardware.services.UserService;
import io.swagger.annotations.Api;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/auth")
@Api(value = "AuthController",description = "APIS for login Authentication!!")
public class AuthControllers {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtHelper jwtHelper;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request){
//        username is email
        UsernamePasswordAuthenticationToken authentication=new UsernamePasswordAuthenticationToken(request.getEmail(),request.getPassword());
        try {
            Authentication authenticate = authenticationManager.authenticate(authentication);
        }catch (BadCredentialsException e){
            throw new BadApiRequestException("Invalid username or password!!");
        }
        UserDetails userDetails= userDetailsService.loadUserByUsername(request.getEmail());;
        String generatedToken = this.jwtHelper.generateToken(userDetails);
        JwtResponse response = JwtResponse.builder()
                .jwtToken(generatedToken)
                .user(modelMapper.map(userDetails, UserDto.class))
                .build();
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @GetMapping("/current")
    public ResponseEntity<UserDto> getCurrentUser(Principal principal){
        String name= principal.getName();
        UserDetails userDetails = userDetailsService.loadUserByUsername(name);
        UserDto userDto = modelMapper.map(userDetails, UserDto.class);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }
}
