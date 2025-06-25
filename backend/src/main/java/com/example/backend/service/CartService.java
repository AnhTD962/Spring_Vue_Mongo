package com.example.backend.service;

import com.example.backend.model.entity.Cart;

import java.util.List;

public interface CartService {

    public Cart saveCart(String productId, String userId);

    public List<Cart> getCartsByUser(String userId);

    public Integer getCountCart(String userId);

    public void updateQuantity(String sy, String cid);

}
