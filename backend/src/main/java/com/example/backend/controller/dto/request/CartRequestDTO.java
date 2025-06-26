package com.example.backend.controller.dto.request;

import lombok.Data;

@Data
public class CartRequestDTO {
    private String pid;
    private String uid;
    Integer quantity;
}
