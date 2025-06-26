package com.example.backend.service.impl;

import com.example.backend.controller.dto.response.CartListResponseDTO;
import com.example.backend.controller.dto.response.CartResponseDTO;
import com.example.backend.model.entity.Cart;
import com.example.backend.model.entity.Product;
import com.example.backend.model.entity.User;
import com.example.backend.repository.CartRepository;
import com.example.backend.repository.ProductRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Cart saveCart(String productId, String userId, Integer quantity) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Default quantity to 1 if invalid (null or less than 1)
        int qty = (quantity == null || quantity < 1) ? 1 : quantity;

        // Check if product already exists in user's cart
        List<Cart> existingCart = cartRepository.findByProductIdAndUserId(productId, userId);

        Cart newCart = new Cart();
        newCart.setUser(user);
        newCart.setProduct(product);
        newCart.setQuantity(qty);
        newCart.setTotalPrice(qty * product.getDiscountPrice());
        return cartRepository.save(newCart);

    }


    @Override
    public CartListResponseDTO getCartWithTotal(String userId) {
        List<Cart> carts = cartRepository.findByUserId(userId);

        List<CartResponseDTO> items = new ArrayList<>();

        for (Cart cart : carts) {
            Double total = cart.getTotalPrice();
            if (total == null) {
                // Fix nếu bị null (fallback)
                double fallbackPrice = cart.getQuantity() * (
                        cart.getProduct() != null && cart.getProduct().getDiscountPrice() != null
                                ? cart.getProduct().getDiscountPrice()
                                : 0.0
                );
                total = fallbackPrice;
            }

            items.add(new CartResponseDTO(
                    cart.getId(),
                    cart.getProduct() != null ? cart.getProduct().getId() : null,
                    cart.getProduct() != null ? cart.getProduct().getTitle() : "Unknown",
                    cart.getProduct() != null ? cart.getProduct().getCategory() : "Unknown",
                    cart.getQuantity(),
                    total
            ));
        }

        double totalCartPrice = items.stream()
                .mapToDouble(item -> item.getTotalPrice() != null ? item.getTotalPrice() : 0.0)
                .sum();

        return new CartListResponseDTO(items, totalCartPrice);
    }

    @Override
    public Integer getCountCart(String userId) {
        Integer countByUserId = cartRepository.countByUserId(userId);
        return countByUserId;
    }

    @Override
    public void updateCartQuantityById(String cartId, int quantity) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        if (quantity < 1) quantity = 1;

        cart.setQuantity(quantity);
        cart.setTotalPrice(quantity * cart.getProduct().getDiscountPrice());

        cartRepository.save(cart);
    }

    @Override
    public void deleteCartById(String cartId) {
        cartRepository.deleteById(cartId);
    }

}
