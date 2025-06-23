package com.example.backend.model.entity;

//import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "orders")
//@Table(name = "orders")
//@Entity
public class Order {

    @Id
    private String id;

    @Field("order_id")
    private String orderId;

    @Field("order_date")
    private LocalDate orderDate;

    @DBRef
    private Product product;

    private Double price;

    private Integer quantity;

    @DBRef
    private User user;

    private String status;

    @Field("payment_type")
    private String paymentType;

    @DBRef
    private OrderAddress orderAddress;

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer id;
//
//    private String orderId;
//
//    private LocalDate orderDate;
//
//    @ManyToOne
//    private Product product;
//
//    private Double price;
//
//    private Integer quantity;
//
//    @ManyToOne
//    private User user;
//
//    private String status;
//
//    private String paymentType;
//
//    @OneToOne(cascade = CascadeType.ALL)
//    private OrderAddress orderAddress;
}
