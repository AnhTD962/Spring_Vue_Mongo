package com.example.backend.controller;

import com.example.backend.controller.dto.request.BulkUpdateStatusRequest;
import com.example.backend.controller.dto.request.OrderRequestDTO;
import com.example.backend.controller.dto.response.OrderDetailResponseDTO;
import com.example.backend.model.entity.Order;
import com.example.backend.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{orderId}")
    public Order getOrderByIdAdmin(@PathVariable String orderId) {
        return orderService.getOrderOrThrow(orderId);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{orderId}/status")
    public String updateOrderStatus(@PathVariable String orderId,
                                    @RequestParam Integer st) {
        return orderService.updateOrderStatusOrThrow(orderId, st);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/search")
    public Order searchOrder(@RequestParam String orderId) {
        return orderService.searchOrderByOrderId(orderId);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/bulk-update-status-by-ids")
    public String bulkUpdateByIds(@RequestBody BulkUpdateStatusRequest request) {
        return orderService.bulkUpdateByIds(request.getOrderIds(), request.getNewStatus());
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/my-orders")
    public List<Order> getMyOrders(Principal principal) {
        return orderService.getOrdersByPrincipal(principal);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/place-order")
    public String placeOrder(@RequestBody OrderRequestDTO request, Principal principal) throws Exception {
        return orderService.placeOrderFromPrincipal(principal, request);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/my-orders/{id}")
    public OrderDetailResponseDTO getOrderByIdUser(@PathVariable String id) {
        return orderService.getOrderDetail(id);
    }
}
