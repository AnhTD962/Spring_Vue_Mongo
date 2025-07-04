package com.example.backend.controller.user;

import com.example.backend.controller.dto.request.OrderRequestDTO;
import com.example.backend.controller.dto.response.OrderDetailResponseDTO;
import com.example.backend.model.entity.Order;
import com.example.backend.model.entity.User;
import com.example.backend.service.OrderService;
import com.example.backend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class UserOrderController {

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

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

    @GetMapping("/orders/{id}")
    public ResponseEntity<OrderDetailResponseDTO> getOrderById(@PathVariable String id) {
        return ResponseEntity.ok(orderService.getOrderDetail(id));
    }
}
