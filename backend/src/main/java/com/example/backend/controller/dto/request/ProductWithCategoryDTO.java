package com.example.backend.controller.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductWithCategoryDTO {
    private String id;
    private String title;
    private String description;
    private Double price;
    private int stock;
    private int discount;
    private Double discountPrice;
    private Boolean isActive;

    private String categoryId;
    private String categoryName;
    private String categoryImage;
}
