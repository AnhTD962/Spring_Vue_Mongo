package com.example.backend.repository;

import com.example.backend.model.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface OrderRepository extends MongoRepository<Order, String> {

    Page<Order> findByUserIdOrderByOrderDateDesc(String userId, Pageable pageable);

    Optional<Order> findByOrderId(String orderId);

    Page<Order> findByUserIdAndStatusOrderByOrderDateDesc(String userId, Pageable pageable, String status);

    Page<Order> findByStatusOrderByOrderDateDesc(Pageable pageable, String status);
}
