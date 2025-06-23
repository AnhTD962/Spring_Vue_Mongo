package com.example.backend.service;

import com.example.backend.controller.dto.request.OrderRequestDTO;
import com.example.backend.model.entity.Order;
import org.springframework.data.domain.Page;

import java.util.List;

public interface OrderService {

    public void saveOrder(Integer userid, OrderRequestDTO orderRequest) throws Exception;

    public List<Order> getOrdersByUser(Integer userId);

    public Order updateOrderStatus(Integer id, String status);

    public List<Order> getAllOrders();

    public Order getOrdersByOrderId(String orderId);

    public Page<Order> getAllOrdersPagination(Integer pageNo, Integer pageSize);
}
