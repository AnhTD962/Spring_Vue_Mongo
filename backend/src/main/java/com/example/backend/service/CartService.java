package com.example.backend.service;

import com.example.backend.controller.dto.response.CartListResponseDTO;
import com.example.backend.model.entity.Cart;

import java.security.Principal;

public interface CartService {

    Cart saveCart(String productId, Integer quantity, Principal principal);

    CartListResponseDTO getCartWithTotal(String userId);

    Integer getCountCart(String userId);

    void updateCartQuantityById(String cartId, int quantity);

    void deleteCartById(String cartId);

}
