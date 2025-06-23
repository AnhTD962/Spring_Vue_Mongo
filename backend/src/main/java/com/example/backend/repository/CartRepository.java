package com.example.backend.repository;

import com.example.backend.model.entity.Cart;
import com.example.backend.model.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CartRepository extends MongoRepository<Cart, String> {

    Cart findByProductIdAndUserId(Integer productId, Integer userId);

    Integer countByUserId(Integer userId);

    List<Cart> findByUserId(Integer userId);

    void deleteByUser(User user);
}
