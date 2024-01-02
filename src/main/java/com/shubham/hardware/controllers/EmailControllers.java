package com.shubham.hardware.controllers;

import com.shubham.hardware.dtos.EmailRequest;
import com.shubham.hardware.services.EmailService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
@RequestMapping("/shubham-hardware/email")
@Tag(name = "EmailControllers",description = "Sending Email from our App")
@SecurityRequirement(name = "bearerScheme")
@Slf4j
public class EmailControllers {
    
    @Autowired
    private EmailService emailService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/send")
    public String sendEmailWithoutAttachment(@RequestBody EmailRequest request){
        log.info("Email : {}", Arrays.asList(request.getToEmail()));
        return emailService.sendEmailWithoutAttachment(request);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/send-with-attachment")
    public String sendEmailWithAttachment(@RequestBody EmailRequest request) throws MessagingException {
        log.info("Email : {}",Arrays.asList(request.getToEmail()));
        return emailService.sendEmailWithAttachment(request);
    }
}
