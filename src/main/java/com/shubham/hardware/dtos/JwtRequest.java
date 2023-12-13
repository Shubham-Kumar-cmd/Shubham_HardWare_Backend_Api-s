package com.shubham.hardware.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class JwtRequest {

//    username is email
    private String email;

    private String password;
}
