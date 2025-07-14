package com.example.backend.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CartResponseDTO {
    private String id; // cartId
    private String productImage;
    private String productId;
    private String title;
    private String category;
    private Integer quantity;
    private Double totalPrice;
}