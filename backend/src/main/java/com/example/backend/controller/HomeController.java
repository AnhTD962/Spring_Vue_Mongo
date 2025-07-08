package com.example.backend.controller;

import com.example.backend.model.entity.Category;
import com.example.backend.model.entity.Product;
import com.example.backend.service.CategoryService;
import com.example.backend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class HomeController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @GetMapping("/categories")
    public List<Category> getAllCategories() {
        return categoryService.getAllActiveCategory();
    }

    @GetMapping("/products")
    public List<Product> getProducts() {
        return productService.findByIsActiveTrue();
    }

    @GetMapping("/products/{id}")
    public Product getProduct(@PathVariable String id) {
        return productService.getProductById(id);
    }

    @GetMapping("/categories/{categoryName}")
    public List<Product> getProductsByCategory(@PathVariable String categoryName) {
        return productService.getProductsByCategory(categoryName);
    }
}
