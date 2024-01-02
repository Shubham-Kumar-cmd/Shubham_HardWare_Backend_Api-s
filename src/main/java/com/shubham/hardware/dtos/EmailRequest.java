package com.shubham.hardware.dtos;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EmailRequest {

    private String[] toEmail;
    private String subject;
    private String body;
    private String attachment;
}
