package com.example.backend.controller;

import com.example.backend.exception.BusinessException;
import com.example.backend.exception.NotFoundException;
import com.example.backend.model.entity.Product;
import com.example.backend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/all")
    public List<Product> getProducts() {
        return productService.findByIsActiveTrue();
    }

    @GetMapping("/id/{id}")
    public Product getProduct(@PathVariable String id) {
        return productService.getProductById(id);
    }

    @GetMapping("/category/{categoryName}")
    public List<Product> getProductsByCategory(@PathVariable String categoryName) {
        return productService.getProductsByCategory(categoryName);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public Product saveProduct(@ModelAttribute Product product,
                               @RequestParam(value = "file", required = false) MultipartFile image) {
        return productService.saveProduct(product, image);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable String id,
                                 @ModelAttribute Product product,
                                 @RequestParam(value = "file", required = false) MultipartFile image) {
        return productService.updateProductById(id, product, image);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public List<Product> getProducts(@RequestParam(defaultValue = "") String ch) {
        return productService.getProducts(ch);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public String deleteProduct(@PathVariable String id) {
        return productService.deleteProductOrThrow(id);
    }
}
