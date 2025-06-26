package com.example.backend.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {

    @DBRef
    private Product product;

    private Integer quantity;

    private Double price;
    private Double total;
}
