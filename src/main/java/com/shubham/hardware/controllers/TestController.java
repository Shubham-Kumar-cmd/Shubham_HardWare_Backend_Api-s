package com.shubham.hardware.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "TestController",description = "Welcome API")
@SecurityRequirement(name = "bearerScheme")
public class TestController {

    @GetMapping("/welcome/shubham-hardware")
    public String test(){
        return "Welcome to Shubham Hardware";
    }
}
