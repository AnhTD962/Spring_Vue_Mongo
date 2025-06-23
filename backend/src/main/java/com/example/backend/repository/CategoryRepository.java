package com.example.backend.repository;

import com.example.backend.model.entity.Category;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CategoryRepository extends MongoRepository<Category, String> {
    Boolean existsByName(String name);

    List<Category> findByIsActiveTrue();
}
