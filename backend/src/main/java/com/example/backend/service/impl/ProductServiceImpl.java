package com.example.backend.service.impl;

import com.example.backend.exception.NotFoundException;
import com.example.backend.model.entity.Product;
import com.example.backend.repository.ProductRepository;
import com.example.backend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Product saveProduct(Product product, MultipartFile image) {
        if (product.getDiscount() < 0 || product.getDiscount() > 100) {
            throw new IllegalArgumentException("Invalid discount value");
        }

        double discount = product.getDiscount();
        double discountAmount = product.getPrice() * (discount / 100.0);
        product.setDiscountPrice(product.getPrice() - discountAmount);

        if (image != null && !image.isEmpty()) {
            try {
                String ext = image.getOriginalFilename().substring(image.getOriginalFilename().lastIndexOf("."));
                String uniqueName = UUID.randomUUID() + ext;

                Path uploadDir = Paths.get("uploads/product_img/");
                if (!Files.exists(uploadDir)) {
                    Files.createDirectories(uploadDir);
                }

                Path path = uploadDir.resolve(uniqueName);
                Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                product.setImage(uniqueName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            product.setImage("default.jpg");
        }

        return productRepository.save(product);
    }

    @Override
    public String deleteProductOrThrow(String id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found with ID: " + id));

        productRepository.delete(product);
        return "Product deleted successfully";
    }

    @Override
    public Product getProductOrThrow(String id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found with ID: " + id));
    }

    @Override
    public Product getProductById(String id) {
        return productRepository.findById(id).orElse(null);
    }

    @Override
    public List<Product> getProductsByCategory(String categoryName) {
        return productRepository.findByCategory(categoryName);
    }

    @Override
    public Product updateProductById(String id, Product updated, MultipartFile image) {
        Product dbProduct = getProductById(id);
        if (dbProduct == null) {
            throw new NotFoundException("Product not found with ID: " + id);
        }

        dbProduct.setTitle(updated.getTitle());
        dbProduct.setDescription(updated.getDescription());
        dbProduct.setCategory(updated.getCategory());
        dbProduct.setPrice(updated.getPrice());
        dbProduct.setStock(updated.getStock());
        dbProduct.setIsActive(updated.getIsActive());

        double discount = updated.getDiscount() != null ? updated.getDiscount() : 0.0;
        dbProduct.setDiscount(discount);

        if (updated.getPrice() != null) {
            double discountAmount = updated.getPrice() * (discount / 100.0);
            dbProduct.setDiscountPrice(updated.getPrice() - discountAmount);
        }

        if (image != null && !image.isEmpty()) {
            try {
                String ext = image.getOriginalFilename().substring(image.getOriginalFilename().lastIndexOf("."));
                String uniqueName = UUID.randomUUID() + ext;

                Path uploadDir = Paths.get("uploads/product_img/");
                if (!Files.exists(uploadDir)) {
                    Files.createDirectories(uploadDir);
                }

                Path path = uploadDir.resolve(uniqueName);
                Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                dbProduct.setImage(uniqueName);
            } catch (Exception e) {
                throw new RuntimeException("Failed to upload image", e);
            }
        }

        return productRepository.save(dbProduct);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> searchProduct(String ch) {
        return productRepository.findByTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase(ch, ch);
    }

    @Override
    public List<Product> getProducts(String ch) {
        if (ch == null || ch.trim().isEmpty()) {
            return getAllProducts();
        }
        return searchProduct(ch);
    }

    @Override
    public List<Product> findByIsActiveTrue() {
        return productRepository.findByIsActiveTrue();
    }

}
