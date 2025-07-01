package com.example.backend.service;

import com.example.backend.model.entity.Category;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface CategoryService {
    Category saveCategory(Category category, MultipartFile file) throws IOException;

    List<Category> getAllCategory();

    Boolean existCategory(String name);

    Boolean deleteCategory(String id);

    Category getCategoryById(String id);

    List<Category> getAllActiveCategory();

    Category updateCategory(String id, Category category, MultipartFile file) throws IOException;
}
