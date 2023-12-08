package com.shubham.hardware.dtos;

import com.shubham.hardware.validate.ImageNameValid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDto {

    private String productId;

    @NotBlank(message = "Product title required!!")
    @Size(min = 5,message = "product title should be more than 5 characters!!")
    private String title;

    @NotBlank(message = "required!!")
    private String description;

    private int price;
    private int discountedPrice;
    private int quantity;
    private Date addedDate;
    private boolean live;
    private boolean stock;

//    @ImageNameValid(message = "Image required!!")
    private String productImage;

}
