package com.example.backend.controller.admin;

import com.example.backend.model.entity.Order;
import com.example.backend.model.enums.OrderStatus;
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
        OrderStatus[] values = OrderStatus.values();
        String status = null;
        for (OrderStatus orderSt : values) {
            if (orderSt.getId() == st) {
                status = orderSt.getName();
                break;
            }
        }

        if (status == null) {
            return ResponseEntity.badRequest().body("Invalid status value");
        }

        Order updateOrder = orderService.updateOrderStatus(orderId, status);

        try {
            commonUtil.sendMailForProductOrder(updateOrder, status);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!ObjectUtils.isEmpty(updateOrder)) {
            return ResponseEntity.ok("Status Updated");
        } else {
            return ResponseEntity.internalServerError().body("Status not updated");
        }
    }

    @GetMapping("/orders/search")
    public ResponseEntity<?> searchOrder(@RequestParam String orderId) {
        if (orderId == null || orderId.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Order ID is required");
        }

        Order order = orderService.getOrdersByOrderId(orderId.trim());
        if (ObjectUtils.isEmpty(order)) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(order);
        }
    }
}
