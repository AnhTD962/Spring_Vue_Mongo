package com.example.backend.model.entity;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "products")
public class Product {

    @Id
    private String id;

    @Field("title")
    private String title;

    @Field("description")
    private String description;

    @Field("category")
    private String category;

    @Field("price")
    private Double price;

    @Field("stock")
    private Integer stock;

    @Transient
    @Field("image")
    private String image;

    @Field("discount")
    private Double discount;

    @Field("discount_price")
    private Double discountPrice;

    @Field("is_active")
    private Boolean isActive;

}