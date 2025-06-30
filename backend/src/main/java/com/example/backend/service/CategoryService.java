package com.example.backend.service;

import com.example.backend.model.entity.Category;

import java.util.List;

public interface CategoryService {

    public Category saveCategory(Category category);

    public Boolean existCategory(String name);

    public List<Category> getAllCategory();

    public Boolean deleteCategory(String id);

    public Category getCategoryById(String id);

    public List<Category> getAllActiveCategory();

}
