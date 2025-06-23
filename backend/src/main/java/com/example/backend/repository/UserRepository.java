package com.example.backend.repository;

import com.example.backend.model.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserRepository extends MongoRepository<User, String> {
    User findByEmail(String email);

    List<User> findAll();

    List<User> findByRole(String role);

    User findByResetToken(String token);

    Boolean existsByEmail(String email);
}
