package com.example.backend.service;

import com.example.backend.controller.dto.request.OrderRequestDTO;
import com.example.backend.controller.dto.response.OrderDetailResponseDTO;
import com.example.backend.model.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.security.Principal;
import java.util.List;

public interface OrderService {
    void saveOrder(String userId, OrderRequestDTO orderRequest) throws Exception;

    Page<Order> getOrdersByUser(String userId, Pageable pageable);

    Page<Order> getAllOrders(Pageable pageable, String status);

    String bulkUpdateByIds(List<String> ids, String newStatusStr);

    Order getOrderOrThrow(String orderId);

    String updateOrderStatusOrThrow(String orderId, Integer statusId);

    Page<Order> getOrdersByPrincipal(Principal principal, Pageable pageable, String status);

    String placeOrderFromPrincipal(Principal principal, OrderRequestDTO request) throws Exception;

    OrderDetailResponseDTO getOrderDetail(String id);
}
