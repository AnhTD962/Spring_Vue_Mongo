package com.example.backend.model.entity;

import com.example.backend.model.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "orders")
public class Order {

    @Id
    private String id;

    @Field("order_id")
    private String orderId;

    @Field("order_date")
    private LocalDateTime orderDate;

    @DBRef
    private User user;

    private List<OrderItem> items;

    private Double total;

    private OrderStatus status;

    @Field("payment_type")
    private String paymentType;

    @DBRef
    private OrderAddress orderAddress;

}
