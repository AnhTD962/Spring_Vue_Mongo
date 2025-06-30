package com.example.backend.controller.admin;

import com.example.backend.model.entity.Product;
import com.example.backend.service.ProductService;
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
public class AdminProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/products")
    public ResponseEntity<?> saveProduct(@ModelAttribute Product product,
                                         @RequestParam(value = "file", required = false) MultipartFile image) throws IOException {
        if (product.getDiscount() < 0 || product.getDiscount() > 100) {
            return ResponseEntity.badRequest().body("Invalid discount value");
        }

        double discountAmount = product.getPrice() * (product.getDiscount() / 100.0);
        double discountedPrice = product.getPrice() - discountAmount;
        product.setDiscountPrice(discountedPrice);

        if (image != null && !image.isEmpty()) {
            String imageName = image.getOriginalFilename();
            product.setImage(imageName);

            Path uploadDir = Paths.get("uploads/product_img/");
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            Path path = uploadDir.resolve(imageName);
            Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        } else {
            product.setImage("default.jpg"); // nếu không upload ảnh
        }

        Product savedProduct = productService.saveProduct(product);
        if (savedProduct != null) {
            return ResponseEntity.ok(savedProduct);
        } else {
            return ResponseEntity.internalServerError().body("Something went wrong");
        }
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable String id) {
        Product product = productService.getProductById(id);
        if (product != null) {
            return ResponseEntity.ok(product);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getProducts(@RequestParam(defaultValue = "") String ch) {
        List<Product> listProducts;
        if (ch != null && !ch.trim().isEmpty()) {
            listProducts = productService.searchProduct(ch);
        } else {
            listProducts = productService.getAllProducts();
        }
        return ResponseEntity.ok(listProducts);
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable String id) {
        boolean deleted = productService.deleteProduct(id);
        if (deleted) {
            return ResponseEntity.ok("Product deleted successfully");
        } else {
            return ResponseEntity.internalServerError().body("Something went wrong");
        }
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable String id,
                                           @ModelAttribute Product product,
                                           @RequestParam(value = "file", required = false) MultipartFile image)
            throws IOException {

        double discount = product.getDiscount() != null ? product.getDiscount() : 0.0;
        if (discount < 0 || discount > 100) {
            return ResponseEntity.badRequest().body("Invalid discount value");
        }

        Product existingProduct = productService.getProductById(id);
        if (existingProduct == null) {
            return ResponseEntity.notFound().build();
        }

        if (product.getTitle() != null) existingProduct.setTitle(product.getTitle());
        if (product.getDescription() != null) existingProduct.setDescription(product.getDescription());
        if (product.getCategory() != null) existingProduct.setCategory(product.getCategory());
        if (product.getPrice() != null) existingProduct.setPrice(product.getPrice());
        if (product.getStock() != null) existingProduct.setStock(product.getStock());
        existingProduct.setDiscount(discount);
        existingProduct.setIsActive(product.getIsActive() != null ? product.getIsActive() : true);

        if (existingProduct.getPrice() == null) {
            return ResponseEntity.badRequest().body("Price is required");
        }

        double discountAmount = existingProduct.getPrice() * (discount / 100.0);
        existingProduct.setDiscountPrice(existingProduct.getPrice() - discountAmount);

        if (image != null && !image.isEmpty()) {
            String imageName = image.getOriginalFilename();
            product.setImage(imageName);

            Path uploadDir = Paths.get("uploads/product_img/");
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            assert imageName != null;
            Path path = uploadDir.resolve(imageName);
            Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        }

        Product updatedProduct = productService.updateProduct(existingProduct, image);
        if (updatedProduct != null) {
            return ResponseEntity.ok(updatedProduct);
        } else {
            return ResponseEntity.internalServerError().body("Failed to update product");
        }
    }

}
