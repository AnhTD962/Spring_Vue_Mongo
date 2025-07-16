package com.example.backend.service;

import com.example.backend.controller.dto.request.OrderRequestDTO;
import com.example.backend.controller.dto.response.OrderDetailResponseDTO;
import com.example.backend.model.entity.Order;

import java.security.Principal;
import java.util.List;

public interface OrderService {
    void saveOrder(String userId, OrderRequestDTO orderRequest) throws Exception;

    List<Order> getOrdersByUser(String userId);

    List<Order> getAllOrders();

    String bulkUpdateByIds(List<String> ids, String newStatusStr);

    Order getOrderOrThrow(String orderId);

    String updateOrderStatusOrThrow(String orderId, Integer statusId);

    Order searchOrderByOrderId(String orderId);

    List<Order> getOrdersByPrincipal(Principal principal);

    String placeOrderFromPrincipal(Principal principal, OrderRequestDTO request) throws Exception;

    OrderDetailResponseDTO getOrderDetail(String id);
}
