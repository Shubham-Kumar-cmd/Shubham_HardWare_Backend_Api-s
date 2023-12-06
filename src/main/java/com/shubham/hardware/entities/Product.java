package com.shubham.hardware.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="products")
public class Product {

    @Id
    private String productId;
    private String productName;
    private String productImage;
    private String aboutProduct;
}
