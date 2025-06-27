package com.example.backend.service;

import com.example.backend.controller.dto.request.OrderRequestDTO;
import com.example.backend.controller.dto.response.OrderDetailResponseDTO;
import com.example.backend.model.entity.Order;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface OrderService {

    public void saveOrder(String userid, OrderRequestDTO orderRequest) throws Exception;

    public List<Order> getOrdersByUser(String userId);

    public Order updateOrderStatus(String id, String status);

    public List<Order> getAllOrders();

    public Order getOrdersByOrderId(String orderId);

    public Page<Order> getAllOrdersPagination(Integer pageNo, Integer pageSize);

    public OrderDetailResponseDTO getOrderDetail(String id);
}
