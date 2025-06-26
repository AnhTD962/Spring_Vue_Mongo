package com.example.backend.controller;

import com.example.backend.controller.dto.request.OrderRequestDTO;
import com.example.backend.model.entity.Cart;
import com.example.backend.model.entity.Order;
import com.example.backend.model.entity.User;
import com.example.backend.model.enums.OrderStatus;
import com.example.backend.service.CartService;
import com.example.backend.service.CategoryService;
import com.example.backend.service.OrderService;
import com.example.backend.service.UserService;
import com.example.backend.util.CommonUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile(Principal principal, HttpServletRequest request) {
        if (principal == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }
        User user = userService.getUserByEmail(principal.getName());
        if (user == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }
        return ResponseEntity.ok(user);
    }

    @PostMapping("/cart/add")
    public ResponseEntity<?> addToCart(@RequestParam String pid, @RequestParam String uid) {
        Cart saveCart = cartService.saveCart(pid, uid);
        if (ObjectUtils.isEmpty(saveCart)) {
            return ResponseEntity.badRequest().body("Product add to cart failed");
        }
        return ResponseEntity.ok(saveCart);
    }

    @GetMapping("/cart")
    public ResponseEntity<List<Cart>> getCart(Principal principal) {
        User user = userService.getUserByEmail(principal.getName());
        return ResponseEntity.ok(cartService.getCartsByUser(user.getId()));
    }

    @PutMapping("/cart/quantity")
    public ResponseEntity<?> updateCartQuantity(@RequestParam String action, @RequestParam String cartId) {
        cartService.updateQuantity(action, cartId);
        return ResponseEntity.ok("Quantity updated");
    }

    @GetMapping("/orders")
    public ResponseEntity<List<Order>> getMyOrders(Principal principal) {
        User user = userService.getUserByEmail(principal.getName());
        return ResponseEntity.ok(orderService.getOrdersByUser(user.getId()));
    }

    @PostMapping("/order")
    public ResponseEntity<?> placeOrder(@RequestBody OrderRequestDTO request, Principal principal) throws Exception {
        User user = userService.getUserByEmail(principal.getName());
        orderService.saveOrder(user.getId(), request);
        return ResponseEntity.ok("Order placed successfully");
    }

    @PutMapping("/order/status")
    public ResponseEntity<?> updateOrderStatus(@RequestParam String id, @RequestParam Integer statusId) {
        OrderStatus[] values = OrderStatus.values();
        String status = null;
        for (OrderStatus orderStatus : values) {
            if (orderStatus.getId().equals(statusId)) {
                status = orderStatus.getName();
            }
        }

        Order order = orderService.updateOrderStatus(id, status);
        try {
            commonUtil.sendMailForProductOrder(order, status);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(order);
    }

    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(
            @ModelAttribute User user,
            @RequestParam(value = "img", required = false) MultipartFile img,
            Principal principal
    ) {
        User dbUser = userService.getUserByEmail(principal.getName());
        if (dbUser == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        // Không update email vì email là cố định để định danh user
        user.setEmail(dbUser.getEmail());

        User updated = userService.updateUserProfile(dbUser, user, img);
        if (updated == null) {
            return ResponseEntity.badRequest().body("Update failed");
        }

        return ResponseEntity.ok(updated);
    }


    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestParam String newPassword, @RequestParam String currentPassword, Principal principal) {
        User user = userService.getUserByEmail(principal.getName());
        boolean matches = passwordEncoder.matches(currentPassword, user.getPassword());
        if (!matches) {
            return ResponseEntity.badRequest().body("Current password is incorrect");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userService.updateUser(user);
        return ResponseEntity.ok("Password changed successfully");
    }
}
