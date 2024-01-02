package com.shubham.hardware.services.impl;

import com.shubham.hardware.dtos.EmailRequest;
import com.shubham.hardware.services.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Arrays;
import java.util.Objects;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sender;

    @Override
    public String sendEmailWithoutAttachment(EmailRequest request) {

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(sender);
        mailMessage.setTo(request.getToEmail());
        mailMessage.setSubject(request.getSubject());
        mailMessage.setText(request.getBody());
        javaMailSender.send(mailMessage);

        return "Email successfully send to : "+ Arrays.toString(request.getToEmail());
    }

    @Override
    public String sendEmailWithAttachment(EmailRequest request) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        MimeMessageHelper helper=new MimeMessageHelper(mimeMessage,true);
        helper.setFrom(sender);
//        helper.setTo(new String[]{"shubhamburnwal.53@gmail.com","ratnapriya.priya12@gmail.com","lucyishita18@yahoo.co.in","hk985252@gmail.com"});
        helper.setTo(request.getToEmail());
        helper.setSubject(request.getSubject());
        helper.setText(request.getBody());

        FileSystemResource file = new FileSystemResource(new File(request.getAttachment()));
//        helper.addAttachment(file.getFilename(),file);
        helper.addAttachment(Objects.requireNonNull(file.getFilename()),file);

        javaMailSender.send(mimeMessage);

        return "Mail sent successfully with attachment : "+file.getFilename();
    }
}
