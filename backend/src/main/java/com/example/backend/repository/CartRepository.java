package com.example.backend.repository;

import com.example.backend.model.entity.Cart;
import com.example.backend.model.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CartRepository extends MongoRepository<Cart, String> {

    List<Cart> findByProductIdAndUserId(String productId, String userId);

    Integer countByUserId(String userId);

    List<Cart> findByUserId(String userId);

    void deleteByUser(User user);
}
