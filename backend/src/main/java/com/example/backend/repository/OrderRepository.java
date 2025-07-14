package com.example.backend.repository;

import com.example.backend.model.entity.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends MongoRepository<Order, String> {
    List<Order> findByUserIdOrderByOrderDateDesc(String userId);

    Optional<Order> findByOrderId(String orderId);
}
