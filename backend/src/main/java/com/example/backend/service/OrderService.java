package com.example.backend.service;

import com.example.backend.controller.dto.request.OrderRequestDTO;
import com.example.backend.controller.dto.response.OrderDetailResponseDTO;
import com.example.backend.model.entity.Order;
import org.springframework.data.domain.Page;

import java.util.List;

public interface OrderService {
    void saveOrder(String userId, OrderRequestDTO orderRequest) throws Exception;

    List<Order> getOrdersByUser(String userId);

    Order updateOrderStatus(String id, Integer statusId);

    List<Order> getAllOrders();

    Order getOrdersByOrderId(String orderId);

    Page<Order> getAllOrdersPagination(Integer pageNo, Integer pageSize);

    OrderDetailResponseDTO getOrderDetail(String id);

    Order searchOrderByOrderId(String orderId);
}
