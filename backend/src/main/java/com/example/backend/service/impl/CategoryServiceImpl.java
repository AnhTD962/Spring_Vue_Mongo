package com.example.backend.service.impl;

import com.example.backend.model.entity.Category;
import com.example.backend.repository.CategoryRepository;
import com.example.backend.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.List;
import java.util.UUID;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    private final Path uploadDir = Paths.get("uploads/category_img/");

    @Override
    public Category saveCategory(Category category, MultipartFile file) throws IOException {
        if (category.getName() == null || category.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Category name is required");
        }

        String normalizedName = category.getName().trim().toLowerCase();
        category.setName(normalizedName);

        if (existCategory(normalizedName)) {
            throw new IllegalArgumentException("Category name already exists");
        }

        String imageName = "default.jpg";
        if (file != null && !file.isEmpty()) {
            imageName = generateUniqueFileName(file.getOriginalFilename());
            saveFile(file, imageName);
        }

        category.setImageName(imageName);
        category.setIsActive(category.getIsActive() != null ? category.getIsActive() : true);

        return categoryRepository.save(category);
    }

    @Override
    public List<Category> getAllCategory() {
        return categoryRepository.findAll();
    }

    @Override
    public Boolean existCategory(String name) {
        return categoryRepository.existsByName(name.trim().toLowerCase());
    }

    @Override
    public Boolean deleteCategory(String id) {
        Category category = categoryRepository.findById(id).orElse(null);
        if (category != null) {
            categoryRepository.delete(category);
            return true;
        }
        return false;
    }

    @Override
    public Category getCategoryById(String id) {
        return categoryRepository.findById(id).orElse(null);
    }

    @Override
    public List<Category> getAllActiveCategory() {
        return categoryRepository.findByIsActiveTrue();
    }

    @Override
    public Category updateCategory(String id, Category category, MultipartFile file) throws IOException {
        Category existing = getCategoryById(id);
        if (existing == null) {
            throw new IllegalArgumentException("Category not found");
        }

        String normalizedName = category.getName().trim().toLowerCase();
        existing.setName(normalizedName);
        existing.setIsActive(category.getIsActive() != null ? category.getIsActive() : true);

        if (file != null && !file.isEmpty()) {
            String imageName = generateUniqueFileName(file.getOriginalFilename());
            existing.setImageName(imageName);
            saveFile(file, imageName);
        }

        return categoryRepository.save(existing);
    }

    private void saveFile(MultipartFile file, String imageName) throws IOException {
        if (file != null && !file.isEmpty()) {
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            Path path = uploadDir.resolve(imageName);
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
            }
        }
    }

    private String generateUniqueFileName(String originalName) {
        String extension = "";
        if (originalName != null && originalName.contains(".")) {
            extension = originalName.substring(originalName.lastIndexOf("."));
        }
        return UUID.randomUUID().toString() + extension;
    }
}
