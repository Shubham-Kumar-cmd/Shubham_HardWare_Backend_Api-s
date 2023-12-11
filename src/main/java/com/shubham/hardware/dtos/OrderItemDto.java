package com.shubham.hardware.dtos;

import com.shubham.hardware.entities.Order;
import com.shubham.hardware.entities.Product;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItemDto {
    private int orderItemId;
    private int quantity;
    private double totalPrice;
    private ProductDto product;
}
