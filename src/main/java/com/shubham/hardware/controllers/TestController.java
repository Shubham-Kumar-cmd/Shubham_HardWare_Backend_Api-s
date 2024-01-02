package com.shubham.hardware.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "TestController",description = "Welcome API")
@SecurityRequirement(name = "bearerScheme")
@Slf4j
public class TestController {

    @GetMapping("/welcome/shubham-hardware")
    public String test(){
        String welcome = "of Shubham HardWare";
        log.info("This is welcome page : {}", welcome);
        return "Welcome to Shubham Hardware";
    }
}
