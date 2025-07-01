package com.example.backend.controller.admin;

import com.example.backend.model.entity.Order;
import com.example.backend.service.OrderService;
import com.example.backend.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class AdminOrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CommonUtil commonUtil;

    @GetMapping("/orders")
    public ResponseEntity<Page<Order>> getAllOrders(@RequestParam(name = "pageNo", defaultValue = "0") Integer pageNo,
                                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Page<Order> page = orderService.getAllOrdersPagination(pageNo, pageSize);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/orders/{orderId}")
    public ResponseEntity<Order> getOrderById(@PathVariable String orderId) {
        Order order = orderService.getOrdersByOrderId(orderId);
        if (ObjectUtils.isEmpty(order)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(order);
    }

    @PutMapping("/orders/{orderId}/status")
    public ResponseEntity<String> updateOrderStatus(@PathVariable String orderId, @RequestParam Integer st) {
        try {
            orderService.updateOrderStatus(orderId, st);
            return ResponseEntity.ok("Status Updated");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Status not updated");
        }
    }

    @GetMapping("/orders/search")
    public ResponseEntity<Order> searchOrder(@RequestParam String orderId) {
        try {
            Order order = orderService.searchOrderByOrderId(orderId);
            return ResponseEntity.ok(order);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
