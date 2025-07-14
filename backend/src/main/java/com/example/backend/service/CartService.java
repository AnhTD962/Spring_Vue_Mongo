package com.example.backend.service;

import com.example.backend.controller.dto.response.CartListResponseDTO;
import com.example.backend.model.entity.Cart;

import java.security.Principal;
import java.util.List;

public interface CartService {

    public Cart saveCart(String productId, Integer quantity, Principal principal);

    public CartListResponseDTO getCartWithTotal(String userId);

    public Integer getCountCart(String userId);

    public void updateCartQuantityById(String cartId, int quantity);

    public void deleteCartById(String cartId);

}
