package com.shubham.hardware.controllers;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value = "TestController",description = "Welcome API")
public class TestController {

    @GetMapping("/welcome/shubham-hardware")
    public String test(){
        return "Welcome to Shubham Hardware";
    }
}
