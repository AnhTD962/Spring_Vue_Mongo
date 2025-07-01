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
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public Cart saveCart(String productId, String userId, Integer quantity) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        int qty = (quantity == null || quantity < 1) ? 1 : quantity;

        if (product.getStock() < qty) {
            throw new RuntimeException("Not enough stock available");
        }

        List<Cart> existingCartList = cartRepository.findByProductIdAndUserId(productId, userId);
        Cart cart;

        if (!existingCartList.isEmpty()) {
            cart = existingCartList.get(0);
            int newQuantity = cart.getQuantity() + qty;

            if (product.getStock() < newQuantity - cart.getQuantity()) {
                throw new RuntimeException("Not enough stock for update");
            }

            cart.setQuantity(newQuantity);
            cart.setTotalPrice(newQuantity * product.getDiscountPrice());
        } else {
            cart = new Cart();
            cart.setUser(user);
            cart.setProduct(product);
            cart.setQuantity(qty);
            cart.setTotalPrice(qty * product.getDiscountPrice());
        }

        product.setStock(product.getStock() - qty);
        productRepository.save(product);

        return cartRepository.save(cart);
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
    @Transactional
    public void updateCartQuantityById(String cartId, int newQuantity) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        Product product = cart.getProduct();
        int currentQuantity = cart.getQuantity();

        if (newQuantity < 1) newQuantity = 1;

        int difference = newQuantity - currentQuantity;

        if (difference > 0) {
            if (product.getStock() < difference) {
                throw new RuntimeException("Not enough stock available");
            }
            product.setStock(product.getStock() - difference);
        } else if (difference < 0) {
            product.setStock(product.getStock() + (-difference));
        }

        cart.setQuantity(newQuantity);
        cart.setTotalPrice(newQuantity * product.getDiscountPrice());

        productRepository.save(product);
        cartRepository.save(cart);
    }


    @Override
    @Transactional
    public void deleteCartById(String cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        Product product = cart.getProduct();
        int quantity = cart.getQuantity();

        product.setStock(product.getStock() + quantity);
        productRepository.save(product);

        cartRepository.deleteById(cartId);
    }

}
