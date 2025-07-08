package com.example.backend.controller.admin;

import com.example.backend.exception.NotFoundException;
import com.example.backend.model.entity.Order;
import com.example.backend.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class AdminOrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/orders")
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

    @GetMapping("/orders/{orderId}")
    public Order getOrderById(@PathVariable String orderId) {
        Order order = orderService.getOrdersByOrderId(orderId);
        if (ObjectUtils.isEmpty(order)) {
            throw new NotFoundException("Order not found");
        }
        return order;
    }

    @PutMapping("/orders/{orderId}/status")
    public String updateOrderStatus(@PathVariable String orderId, @RequestParam Integer st) {
        orderService.updateOrderStatus(orderId, st);  // May throw IllegalArgumentException
        return "Status Updated";
    }

    @GetMapping("/orders/search")
    public Order searchOrder(@RequestParam String orderId) {
        return orderService.searchOrderByOrderId(orderId); // May throw exception if not found
    }
}
