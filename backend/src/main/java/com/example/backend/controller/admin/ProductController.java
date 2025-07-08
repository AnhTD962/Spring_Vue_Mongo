package com.example.backend.controller.admin;

import com.example.backend.exception.BusinessException;
import com.example.backend.exception.NotFoundException;
import com.example.backend.model.entity.Product;
import com.example.backend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/products")
    public Product saveProduct(@ModelAttribute Product product,
                               @RequestParam(value = "file", required = false) MultipartFile image) {
        return productService.saveProduct(product, image);
    }

    @PutMapping("/products/{id}")
    public Product updateProduct(@PathVariable String id,
                                 @ModelAttribute Product product,
                                 @RequestParam(value = "file", required = false) MultipartFile image) {
        product.setId(id); // ensure correct ID
        return productService.updateProduct(product, image);
    }

    @GetMapping("/products")
    public List<Product> getProducts(@RequestParam(defaultValue = "") String ch) {
        return (ch != null && !ch.trim().isEmpty())
                ? productService.searchProduct(ch)
                : productService.getAllProducts();
    }

    @GetMapping("/products/{id}")
    public Product getProductById(@PathVariable String id) {
        Product product = productService.getProductById(id);
        if (product == null) {
            throw new NotFoundException("Product not found");
        }
        return product;
    }

    @DeleteMapping("/products/{id}")
    public String deleteProduct(@PathVariable String id) {
        boolean deleted = productService.deleteProduct(id);
        if (!deleted) {
            throw new BusinessException("Failed to delete product");
        }
        return "Product deleted successfully";
    }
}
