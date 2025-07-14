package com.example.backend.controller;

import com.example.backend.controller.dto.request.OrderRequestDTO;
import com.example.backend.controller.dto.response.OrderDetailResponseDTO;
import com.example.backend.exception.NotFoundException;
import com.example.backend.model.entity.Order;
import com.example.backend.model.entity.User;
import com.example.backend.service.OrderService;
import com.example.backend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @GetMapping("/admin/orders")
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/admin/orders/{orderId}")
    public Order getOrderByIdAdmin(@PathVariable String orderId) {
        return orderService.getOrderOrThrow(orderId);
    }

    @PutMapping("/admin/orders/{orderId}/status")
    public String updateOrderStatus(@PathVariable String orderId,
                                    @RequestParam Integer st) {
        return orderService.updateOrderStatusOrThrow(orderId, st);
    }

    @GetMapping("/admin/orders/search")
    public Order searchOrder(@RequestParam String orderId) {
        return orderService.searchOrderByOrderId(orderId);
    }

    @GetMapping("/user/orders")
    public List<Order> getMyOrders(Principal principal) {
        return orderService.getOrdersByPrincipal(principal);
    }

    @PostMapping("/user/order")
    public String placeOrder(@RequestBody OrderRequestDTO request, Principal principal) throws Exception {
        return orderService.placeOrderFromPrincipal(principal, request);
    }

    @GetMapping("/user/orders/{id}")
    public OrderDetailResponseDTO getOrderByIdUser(@PathVariable String id) {
        return orderService.getOrderDetail(id);
    }
}
