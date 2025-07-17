package com.example.backend.controller.dto.request;

import lombok.Data;

@Data
public class CartRequestDTO {
    Integer quantity;
    private String pid;
}
