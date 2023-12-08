package com.shubham.hardware.entities;

import com.shubham.hardware.validate.ImageNameValid;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "categories")
public class Category {
    @Id
    private String categoryId;

    @Column(length = 60, nullable = false,unique = true)
    private String title;

    @Column(length = 500, nullable = false)
    private String description;

    @Column(name = "category_image_name")
    private String coverImage;//coverImageUri
}
