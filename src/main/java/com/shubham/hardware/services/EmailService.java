package com.shubham.hardware.services;

import com.shubham.hardware.dtos.EmailRequest;
import jakarta.mail.MessagingException;

public interface EmailService {

    String sendEmailWithoutAttachment(EmailRequest request);
    String sendEmailWithAttachment(EmailRequest request) throws MessagingException;
}
