package com.example.backend.service;

import com.example.backend.model.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface CategoryService {
    Category saveCategory(Category category, MultipartFile file) throws IOException;

    Page<Category> getAllCategory(Pageable pageable);

    Boolean existCategory(String name);

    Boolean deleteCategory(String id);

    Category getCategoryById(String id);

    Page<Category> getAllActiveCategory(Pageable pageable);

    Category updateCategory(String id, Category category, MultipartFile file) throws IOException;
}
