package com.example.backend.repository;

import com.example.backend.model.entity.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ProductRepository extends MongoRepository<Product, String> {

    List<Product> findByIsActiveTrue();

    List<Product> findAll();

    List<Product> findByCategory(String categoryName);

    List<Product> findByTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase(String ch, String ch2);

}
