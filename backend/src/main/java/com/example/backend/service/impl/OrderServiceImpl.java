package com.example.backend.service.impl;

import com.example.backend.controller.dto.request.OrderRequestDTO;
import com.example.backend.model.entity.*;
import com.example.backend.model.enums.OrderStatus;
import com.example.backend.repository.CartRepository;
import com.example.backend.repository.OrderAddressRepository;
import com.example.backend.repository.OrderRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.OrderService;
import com.example.backend.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderAddressRepository orderAddressRepository;

    @Autowired
    private CommonUtil commonUtil;

    @Override
    public void saveOrder(String userId, OrderRequestDTO orderRequest) throws Exception {
        List<Cart> carts = cartRepository.findByUserId(userId);
        if (carts.isEmpty()) {
            throw new IllegalStateException("Cart is empty.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        OrderAddress address = new OrderAddress();
        address.setFirstName(orderRequest.getFirstName());
        address.setLastName(orderRequest.getLastName());
        address.setEmail(orderRequest.getEmail());
        address.setMobileNo(orderRequest.getMobileNo());
        address.setAddress(orderRequest.getAddress());
        address.setCity(orderRequest.getCity());
        address.setState(orderRequest.getState());
        orderAddressRepository.save(address);

        List<OrderItem> items = new ArrayList<>();
        double total = 0;

        for (Cart cart : carts) {
            OrderItem item = new OrderItem();
            item.setProduct(cart.getProduct());
            item.setQuantity(cart.getQuantity());
            item.setPrice(cart.getProduct().getDiscountPrice());
            item.setTotal(item.getQuantity() * item.getPrice());
            items.add(item);
            total += item.getTotal();
        }

        Order order = new Order();
        order.setOrderId(UUID.randomUUID().toString());
        order.setOrderDate(LocalDateTime.now());
        order.setUser(user);
        order.setItems(items);
        order.setTotal(total);
        order.setStatus(OrderStatus.IN_PROGRESS);
        order.setPaymentType(orderRequest.getPaymentType());
        order.setOrderAddress(address);

        Order savedOrder = orderRepository.save(order);
        cartRepository.deleteAll(carts); // clear cart
        commonUtil.sendMailForProductOrder(savedOrder, "In Progress");
    }

    @Override
    public List<Order> getOrdersByUser(String userId) {
        return orderRepository.findByUserId(userId);
    }

    @Override
    public Order updateOrderStatus(String id, String status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(OrderStatus.valueOf(status));
        return orderRepository.save(order);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Order getOrdersByOrderId(String orderId) {
        return orderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with orderId: " + orderId));
    }

    @Override
    public Page<Order> getAllOrdersPagination(Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("orderDate").descending());
        return orderRepository.findAll(pageable);
    }
}
