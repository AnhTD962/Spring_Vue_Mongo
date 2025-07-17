package com.example.backend.controller;

import com.example.backend.controller.dto.request.CartRequestDTO;
import com.example.backend.controller.dto.response.CartListResponseDTO;
import com.example.backend.model.entity.Cart;
import com.example.backend.model.entity.User;
import com.example.backend.service.CartService;
import com.example.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@PreAuthorize("hasRole('ROLE_USER')")
@RequestMapping("/api/cart")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class CartController {

    @Autowired
    private UserService userService;

    @Autowired
    private CartService cartService;

    @PostMapping
    public Cart addToCart(@RequestBody CartRequestDTO request, Principal principal) {
        return cartService.saveCart(
                request.getPid(),
                request.getQuantity(),
                principal
        );
    }

    @GetMapping
    public CartListResponseDTO getCart(Principal principal) {
        User user = userService.getUserByEmail(principal.getName());
        return cartService.getCartWithTotal(user.getId());
    }

    @PutMapping("/{cartId}")
    public void updateCartQuantity(@PathVariable String cartId,
                                   @RequestParam int quantity) {
        cartService.updateCartQuantityById(cartId, quantity);
    }

    @GetMapping("/count")
    public int getCountCart(Principal principal) {
        User user = userService.getUserByEmail(principal.getName());
        return cartService.getCountCart(user.getId());
    }

    @DeleteMapping("/{cartId}")
    public void deleteCartById(@PathVariable String cartId) {
        cartService.deleteCartById(cartId);
    }
}
