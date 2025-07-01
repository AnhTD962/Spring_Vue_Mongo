package com.example.backend.controller.admin;

import com.example.backend.model.entity.Product;
import com.example.backend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class AdminProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/products")
    public ResponseEntity<Product> saveProduct(@ModelAttribute Product product,
                                               @RequestParam(value = "file", required = false) MultipartFile image) {
        try {
            Product saved = productService.saveProduct(product, image);
            return ResponseEntity.ok(saved);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build(); // invalid input
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build(); // internal error
        }
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable String id,
                                                 @ModelAttribute Product product,
                                                 @RequestParam(value = "file", required = false) MultipartFile image) {
        try {
            product.setId(id); // make sure ID is passed correctly
            Product updated = productService.updateProduct(product, image);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getProducts(@RequestParam(defaultValue = "") String ch) {
        List<Product> products = (ch != null && !ch.trim().isEmpty())
                ? productService.searchProduct(ch)
                : productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable String id) {
        Product product = productService.getProductById(id);
        return (product != null)
                ? ResponseEntity.ok(product)
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable String id) {
        boolean deleted = productService.deleteProduct(id);
        return deleted
                ? ResponseEntity.ok("Product deleted successfully")
                : ResponseEntity.internalServerError().body("Failed to delete product");
    }
}
