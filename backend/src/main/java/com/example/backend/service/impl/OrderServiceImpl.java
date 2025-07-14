package com.example.backend.service.impl;

import com.example.backend.controller.dto.request.OrderRequestDTO;
import com.example.backend.controller.dto.response.OrderDetailResponseDTO;
import com.example.backend.exception.NotFoundException;
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
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
    @Transactional(rollbackFor = Exception.class)
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
        cartRepository.deleteAll(carts);
        commonUtil.sendMailForProductOrder(savedOrder, "In Progress");
    }


    @Override
    public List<Order> getOrdersByUser(String userId) {
        return orderRepository.findByUserIdOrderByOrderDateDesc(userId);
    }


    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public OrderDetailResponseDTO getOrderDetail(String id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        OrderDetailResponseDTO dto = new OrderDetailResponseDTO();
        dto.setId(order.getId());
        dto.setOrderId(order.getOrderId());
        dto.setOrderDate(order.getOrderDate());
        dto.setStatus(order.getStatus().name());
        dto.setPaymentType(order.getPaymentType());
        dto.setTotal(order.getTotal());

        // User info
        OrderDetailResponseDTO.UserDTO userDto = new OrderDetailResponseDTO.UserDTO();
        userDto.setName(order.getUser().getName());
        userDto.setEmail(order.getUser().getEmail());
        userDto.setMobileNumber(order.getUser().getMobileNumber());
        dto.setUser(userDto);

        // Address info
        OrderAddress a = order.getOrderAddress();
        OrderDetailResponseDTO.AddressDTO addressDto = new OrderDetailResponseDTO.AddressDTO();
        addressDto.setFirstName(a.getFirstName());
        addressDto.setLastName(a.getLastName());
        addressDto.setEmail(a.getEmail());
        addressDto.setMobileNo(a.getMobileNo());
        addressDto.setAddress(a.getAddress());
        addressDto.setCity(a.getCity());
        addressDto.setState(a.getState());
        dto.setAddress(addressDto);

        // Items
        List<OrderDetailResponseDTO.ItemDTO> itemDTOs = order.getItems().stream().map(item -> {
            OrderDetailResponseDTO.ItemDTO i = new OrderDetailResponseDTO.ItemDTO();
            i.setProductId(item.getProduct().getId());
            i.setProductName(item.getProduct().getTitle());
            i.setPrice(item.getPrice());
            i.setQuantity(item.getQuantity());
            i.setTotal(item.getTotal());
            return i;
        }).toList();

        dto.setItems(itemDTOs);
        return dto;
    }

    @Override
    public Order getOrderOrThrow(String orderId) {
        return orderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found"));
    }

    @Override
    public String updateOrderStatusOrThrow(String orderId, Integer statusId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found"));

        OrderStatus matchedStatus = null;
        for (OrderStatus os : OrderStatus.values()) {
            if (os.getId() == statusId) {
                matchedStatus = os;
                break;
            }
        }

        if (matchedStatus == null) {
            throw new IllegalArgumentException("Invalid status value");
        }

        order.setStatus(matchedStatus);
        orderRepository.save(order);

        try {
            commonUtil.sendMailForProductOrder(order, matchedStatus.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "Status Updated";
    }

    @Override
    public String placeOrderFromPrincipal(Principal principal, OrderRequestDTO request) throws Exception {
        if (principal == null) {
            throw new RuntimeException("Unauthorized");
        }
        User user = userRepository.findByEmail(principal.getName());
        saveOrder(user.getId(), request);
        return "Order placed successfully";
    }


    @Override
    public List<Order> getOrdersByPrincipal(Principal principal) {
        if (principal == null) {
            throw new RuntimeException("Unauthorized");
        }
        User user = userRepository.findByEmail(principal.getName());
        return getOrdersByUser(user.getId());
    }

    @Override
    public Order searchOrderByOrderId(String orderId) {
        if (orderId == null || orderId.trim().isEmpty()) {
            throw new IllegalArgumentException("Order ID is required");
        }

        return orderRepository.findByOrderId(orderId.trim())
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }
}
