package com.shubham.hardware.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/shubham-hardware")
    public String test(){
        return "Welcome to Shubham Hardware";
    }
}
