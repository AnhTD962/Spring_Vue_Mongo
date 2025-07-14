package com.example.backend.controller;

import com.example.backend.exception.BusinessException;
import com.example.backend.exception.NotFoundException;
import com.example.backend.model.entity.Product;
import com.example.backend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/admin/products")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping
    public Product saveProduct(@ModelAttribute Product product,
                               @RequestParam(value = "file", required = false) MultipartFile image) {
        return productService.saveProduct(product, image);
    }

    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable String id,
                                 @ModelAttribute Product product,
                                 @RequestParam(value = "file", required = false) MultipartFile image) {
        return productService.updateProductById(id, product, image);
    }

    @GetMapping
    public List<Product> getProducts(@RequestParam(defaultValue = "") String ch) {
        return productService.getProducts(ch);
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable String id) {
        return productService.getProductOrThrow(id);
    }

    @DeleteMapping("/{id}")
    public String deleteProduct(@PathVariable String id) {
        return productService.deleteProductOrThrow(id);
    }
}
