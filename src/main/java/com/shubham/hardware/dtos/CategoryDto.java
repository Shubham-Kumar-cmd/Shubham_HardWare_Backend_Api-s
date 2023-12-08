package com.shubham.hardware.dtos;

import com.shubham.hardware.validate.ImageNameValid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryDto {

    private String categoryId;

    @NotBlank(message = "Title is required!!")
    @Size(min = 5, message = "Title must be of minimum 5 characters!!")
    private String title;

    @NotBlank(message = "Description is required!!")
    private String description;

//    @ImageNameValid(message = "Cover image required!!")
    private String coverImage;//coverImageUri
}
