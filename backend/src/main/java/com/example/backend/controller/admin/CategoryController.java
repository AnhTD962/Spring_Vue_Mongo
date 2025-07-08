package com.example.backend.controller.admin;

import com.example.backend.model.entity.Category;
import com.example.backend.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/categories")
    public List<Category> getAllCategories() {
        return categoryService.getAllCategory();
    }

    @GetMapping("/categories/{id}")
    public Category getCategoryById(@PathVariable String id) {
        return categoryService.getCategoryById(id);
    }

    @GetMapping("/categories/active")
    public List<Category> getAllActiveCategories() {
        return categoryService.getAllActiveCategory();
    }

    @PostMapping("/categories")
    public Category saveCategory(@ModelAttribute Category category,
                                 @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
        return categoryService.saveCategory(category, file);
    }

    @PutMapping("/categories/{id}")
    public Category updateCategory(@PathVariable String id,
                                   @ModelAttribute Category category,
                                   @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
        return categoryService.updateCategory(id, category, file);
    }

    @DeleteMapping("/categories/{id}")
    public String deleteCategory(@PathVariable String id) {
        boolean deleted = categoryService.deleteCategory(id);
        if (!deleted) throw new RuntimeException("Something went wrong");
        return "Category deleted successfully";
    }
}
