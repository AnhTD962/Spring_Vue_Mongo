package com.example.backend.service.impl;

import com.example.backend.controller.dto.request.OrderRequestDTO;
import com.example.backend.model.entity.Cart;
import com.example.backend.model.entity.Order;
import com.example.backend.model.entity.OrderAddress;
import com.example.backend.model.entity.User;
import com.example.backend.model.enums.OrderStatus;
import com.example.backend.repository.CartRepository;
import com.example.backend.repository.OrderRepository;
import com.example.backend.service.OrderService;
import com.example.backend.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CommonUtil commonUtil;

    @Override
    public void saveOrder(Integer userid, OrderRequestDTO orderRequest) throws Exception {

        List<Cart> carts = cartRepository.findByUserId(userid);

        for (Cart cart : carts) {

            Order order = new Order();

            order.setOrderId(UUID.randomUUID().toString());
            order.setOrderDate(LocalDate.now());

            order.setProduct(cart.getProduct());
            order.setPrice(cart.getProduct().getDiscountPrice());

            order.setQuantity(cart.getQuantity());
            order.setUser(cart.getUser());

            order.setStatus(OrderStatus.IN_PROGRESS.getName());
            order.setPaymentType(orderRequest.getPaymentType());

            OrderAddress address = new OrderAddress();
            address.setFirstName(orderRequest.getFirstName());
            address.setLastName(orderRequest.getLastName());
            address.setEmail(orderRequest.getEmail());
            address.setMobileNo(orderRequest.getMobileNo());
            address.setAddress(orderRequest.getAddress());
            address.setCity(orderRequest.getCity());
            address.setState(orderRequest.getState());

            order.setOrderAddress(address);

            Order saveOrder = orderRepository.save(order);
            resetCart(cart.getUser());
            commonUtil.sendMailForProductOrder(saveOrder, "success");
        }
    }

    private void resetCart(User user) {
        cartRepository.deleteByUser(user);
    }

    @Override
    public List<Order> getOrdersByUser(Integer userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        return orders;
    }

    @Override
    public Order updateOrderStatus(Integer id, String status) {
        Optional<Order> findById = orderRepository.findById(String.valueOf(id));
        if (findById.isPresent()) {
            Order productOrder = findById.get();
            productOrder.setStatus(status);
            Order updateOrder = orderRepository.save(productOrder);
            return updateOrder;
        }
        return null;
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Page<Order> getAllOrdersPagination(Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return orderRepository.findAll(pageable);

    }

    @Override
    public Order getOrdersByOrderId(String orderId) {
        return orderRepository.findByOrderId(orderId);
    }

}
