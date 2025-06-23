package com.example.backend.model.entity;

//import jakarta.persistence.*;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "carts")
//@Table(name = "carts")
//@Entity
public class Cart {

    @Id
    private String id;

    @DBRef
    private User user;

    @DBRef
    private Product product;

    private Integer quantity;

    @Transient
    private Double totalPrice;

    @Transient
    private Double totalOrderPrice;

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer id;
//
//    @ManyToOne
//    private User user;
//
//    @ManyToOne
//    private Product product;
//
//    private Integer quantity;
//
//    @Transient
//    private Double totalPrice;
//
//    @Transient
//    private Double totalOrderPrice;
}
