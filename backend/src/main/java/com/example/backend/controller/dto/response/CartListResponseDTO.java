package com.example.backend.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CartListResponseDTO {
    private List<CartResponseDTO> items;
    private double totalCartPrice;
}
