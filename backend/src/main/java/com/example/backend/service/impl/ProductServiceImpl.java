package com.example.backend.service.impl;

import com.example.backend.model.entity.Product;
import com.example.backend.repository.ProductRepository;
import com.example.backend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
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
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Boolean deleteProduct(String id) {
        Product product = productRepository.findById(id).orElse(null);

        if (!ObjectUtils.isEmpty(product)) {
            productRepository.delete(product);
            return true;
        }
        return false;
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
    public Product updateProduct(Product product, MultipartFile image) {
        Product dbProduct = getProductById(product.getId());
        if (dbProduct == null) {
            throw new RuntimeException("Product not found with ID: " + product.getId());
        }

        dbProduct.setTitle(product.getTitle());
        dbProduct.setDescription(product.getDescription());
        dbProduct.setCategory(product.getCategory());
        dbProduct.setPrice(product.getPrice());
        dbProduct.setStock(product.getStock());
        dbProduct.setIsActive(product.getIsActive());

        double discount = product.getDiscount() != null ? product.getDiscount() : 0.0;
        dbProduct.setDiscount(discount);
        if (product.getPrice() != null) {
            double discountAmount = product.getPrice() * (discount / 100.0);
            dbProduct.setDiscountPrice(product.getPrice() - discountAmount);
        }

        // Handle image upload
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
                dbProduct.setImage(uniqueName); // update image name in DB
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return productRepository.save(dbProduct);
    }


    @Override
    public List<Product> searchProduct(String ch) {
        return productRepository.findByTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase(ch, ch);
    }

    @Override
    public List<Product> findByIsActiveTrue() {
        return productRepository.findByIsActiveTrue();
    }

}
