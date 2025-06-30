package com.example.backend.controller.user;

import com.example.backend.controller.dto.request.CartRequestDTO;
import com.example.backend.controller.dto.response.CartListResponseDTO;
import com.example.backend.model.entity.Cart;
import com.example.backend.model.entity.User;
import com.example.backend.service.CartService;
import com.example.backend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Slf4j
@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class UserCartController {

    @Autowired
    private UserService userService;

    @Autowired
    private CartService cartService;

    @PostMapping("/cart/add")
    public ResponseEntity<?> addToCart(@RequestBody CartRequestDTO request) {
        Cart saveCart = cartService.saveCart(
                request.getPid(),
                request.getUid(),
                request.getQuantity()
        );
        if (ObjectUtils.isEmpty(saveCart)) {
            return ResponseEntity.badRequest().body("Product add to cart failed");
        }
        return ResponseEntity.ok(saveCart);
    }

    @GetMapping("/cart")
    public ResponseEntity<CartListResponseDTO> getCart(Principal principal) {
        User user = userService.getUserByEmail(principal.getName());
        return ResponseEntity.ok(cartService.getCartWithTotal(user.getId()));
    }

    @PutMapping("/cart/{cartId}")
    public ResponseEntity<?> updateCartQuantity(
            @PathVariable String cartId,
            @RequestParam int quantity
    ) {
        cartService.updateCartQuantityById(cartId, quantity);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/cart/count")
    public ResponseEntity<?> getCountCart(Principal principal) {
        User user = userService.getUserByEmail(principal.getName());
        return ResponseEntity.ok(cartService.getCountCart(user.getId()));
    }

    @DeleteMapping("/cart/{cartId}")
    public ResponseEntity<?> deleteCartById(@PathVariable String cartId) {
        cartService.deleteCartById(cartId);
        return ResponseEntity.ok().build();
    }
}
