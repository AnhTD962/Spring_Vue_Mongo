package com.example.backend.controller.admin;

import com.example.backend.model.entity.Category;
import com.example.backend.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class AdminCategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategory());
    }

    @GetMapping("/categories/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable String id) {
        Category category = categoryService.getCategoryById(id);
        if (category == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(category);
    }


    @GetMapping("/categories/active")
    public ResponseEntity<List<Category>> getAllActiveCategories() {
        return ResponseEntity.ok(categoryService.getAllActiveCategory());
    }

    @PostMapping("/categories")
    public ResponseEntity<?> saveCategory(
            @ModelAttribute Category category,
            @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {

        if (category.getName() == null || category.getName().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Category name is required");
        }

        if (categoryService.existCategory(category.getName())) {
            return ResponseEntity.badRequest().body("Category name already exists");
        }

        String imageName = (file != null && !file.isEmpty()) ? file.getOriginalFilename() : "default.jpg";
        category.setImageName(imageName);

        if (category.getIsActive() == null) {
            category.setIsActive(true);
        }

        Category saved = categoryService.saveCategory(category);

        if (file != null && !file.isEmpty()) {
            Path uploadDir = Paths.get("uploads/category_img/");
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            Path path = uploadDir.resolve(file.getOriginalFilename());
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        }

        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/categories/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable String id) {
        boolean deleted = categoryService.deleteCategory(id);
        if (deleted) {
            return ResponseEntity.ok("Category deleted successfully");
        } else {
            return ResponseEntity.internalServerError().body("Something went wrong");
        }
    }

    @PutMapping("/categories/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable String id,
                                            @ModelAttribute Category category,
                                            @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
        Category existing = categoryService.getCategoryById(id);
        if (existing == null) return ResponseEntity.notFound().build();

        existing.setName(category.getName());
        existing.setIsActive(category.getIsActive() != null ? category.getIsActive() : true);

        String imageName = (file != null && !file.isEmpty()) ? file.getOriginalFilename() : existing.getImageName();
        existing.setImageName(imageName);

        Category updated = categoryService.saveCategory(existing);

        if (file != null && !file.isEmpty()) {
            Path uploadDir = Paths.get("uploads/category_img/");
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            Path path = uploadDir.resolve(imageName);
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        }
        return ResponseEntity.ok(updated);
    }

}
