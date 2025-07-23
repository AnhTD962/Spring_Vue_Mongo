package com.example.backend.repository;

import com.example.backend.model.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends MongoRepository<Category, String> {
    Boolean existsByName(String name);

    Page<Category> findByIsActiveTrue(Pageable pageable);

    Optional<Category> findByIdAndIsActiveTrue(String id);
}
