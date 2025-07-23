package com.example.backend.repository;

import com.example.backend.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserRepository extends MongoRepository<User, String> {

    User findByEmail(String email);

    Page<User> findAll(Pageable pageable);

    Page<User> findByRole(String role, Pageable pageable);

    Boolean existsByEmail(String email);
}
