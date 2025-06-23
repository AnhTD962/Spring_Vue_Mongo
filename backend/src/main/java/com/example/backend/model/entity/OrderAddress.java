package com.example.backend.model.entity;

//import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "order_addresses")
//@Table(name = "orders_address")
//@Entity
public class OrderAddress {

    @Id
    private String id;

    @Field("first_name")
    private String firstName;

    @Field("last_name")
    private String lastName;

    private String email;

    @Field("mobile_no")
    private String mobileNo;

    private String address;

    private String city;

    private String state;

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer id;
//
//    private String firstName;
//
//    private String lastName;
//
//    private String email;
//
//    private String mobileNo;
//
//    private String address;
//
//    private String city;
//
//    private String state;
//
//    private String pincode;
}
