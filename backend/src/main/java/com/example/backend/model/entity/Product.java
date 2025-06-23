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
//@Table(name = "products")
//@Entity
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
    private int stock;

    @Transient
    @Field("image")
    private String image;

    @Field("discount")
    private int discount;

    @Field("discount_price")
    private Double discountPrice;

    @Field("is_active")
    private Boolean isActive;
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer id;
//
//    @Column(length = 500)
//    private String title;
//
//    @Column(length = 5000)
//    private String description;
//
//    private String category;
//
//    private Double price;
//
//    private int stock;
//
//    private String image;
//
//    private int discount;
//
//    private Double discountPrice;
//
//    private Boolean isActive;
}