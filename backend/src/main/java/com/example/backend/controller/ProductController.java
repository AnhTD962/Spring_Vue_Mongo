package com.example.backend.controller;

import com.example.backend.model.entity.Product;
import com.example.backend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/all")
    public Page<Product> getProducts(@RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "5") int size) {
        return productService.findByIsActiveTrue(PageRequest.of(page, size));
    }

    @GetMapping("/id/{id}")
    public Product getProduct(@PathVariable String id) {
        return productService.getProductById(id);
    }

    @GetMapping("/category/{categoryName}")
    public Page<Product> getProductsByCategory(@PathVariable String categoryName,
                                               @RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "5") int size) {
        return productService.getProductsByCategory(categoryName, PageRequest.of(page, size));
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
    public Page<Product> getProducts(@RequestParam(defaultValue = "") String ch,
                                     @RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "5") int size) {
        return productService.getProducts(ch, PageRequest.of(page, size));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public String deleteProduct(@PathVariable String id) {
        return productService.deleteProductOrThrow(id);
    }
}
