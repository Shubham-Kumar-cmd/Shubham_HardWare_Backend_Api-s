package com.shubham.hardware.dtos;

import com.shubham.hardware.validate.ImageNameValid;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private String userId;

    @Size(min = 3, max=35, message = "Invalid Name!!")
    private String name;

    @Pattern(regexp = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$", message = "Invalid User Email!!")
//    @Email(message = "Invalid User Email!!")
    @NotBlank(message = "Email is required!!")
    private String email;

    @Size(min = 6, max = 20, message = "Password should be between 6 to 20 characters!!")
    @NotBlank(message = "Password is required")
    private String password;

    @Size(min = 4, max = 6, message = "Invalid gender!!")
    private String gender;

    private String about;

    @ImageNameValid
    private String imageName;

//    pattern
//    custom validator
}