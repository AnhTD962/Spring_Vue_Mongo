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
import org.springframework.data.domain.Page;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public Map<String, Object> getAllOrders(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize) {

        Page<Order> page = orderService.getAllOrdersPagination(pageNo, pageSize);
        Map<String, Object> response = new HashMap<>();
        response.put("content", page.getContent());
        response.put("totalPages", page.getTotalPages());
        response.put("totalElements", page.getTotalElements());
        response.put("number", page.getNumber());
        response.put("size", page.getSize());

        return response;
    }

    @GetMapping("/admin/orders/{orderId}")
    public Order getOrderByIdAdmin(@PathVariable String orderId) {
        Order order = orderService.getOrdersByOrderId(orderId);
        if (ObjectUtils.isEmpty(order)) {
            throw new NotFoundException("Order not found");
        }
        return order;
    }

    @PutMapping("/admin/orders/{orderId}/status")
    public String updateOrderStatus(@PathVariable String orderId, @RequestParam Integer st) {
        orderService.updateOrderStatus(orderId, st);
        return "Status Updated";
    }

    @GetMapping("/admin/orders/search")
    public Order searchOrder(@RequestParam String orderId) {
        return orderService.searchOrderByOrderId(orderId);
    }

    @GetMapping("/user/orders")
    public List<Order> getMyOrders(Principal principal) {
        User user = userService.getUserByEmail(principal.getName());
        return orderService.getOrdersByUser(user.getId());
    }

    @PostMapping("/user/order")
    public String placeOrder(@RequestBody OrderRequestDTO request, Principal principal) throws Exception {
        User user = userService.getUserByEmail(principal.getName());
        orderService.saveOrder(user.getId(), request);
        return "Order placed successfully";
    }

    @GetMapping("/user/orders/{id}")
    public OrderDetailResponseDTO getOrderByIdUser(@PathVariable String id) {
        return orderService.getOrderDetail(id);
    }
}
