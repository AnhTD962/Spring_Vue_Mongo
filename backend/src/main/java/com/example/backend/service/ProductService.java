package com.example.backend.service;

import com.example.backend.model.entity.Product;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {

    Product saveProduct(Product product, MultipartFile image);

    List<Product> getAllProducts();

    Boolean deleteProduct(String id);

    Product getProductById(String id);

    Product updateProduct(Product product, MultipartFile file);

    List<Product> searchProduct(String ch);

    List<Product> findByIsActiveTrue();

    List<Product> getProductsByCategory(String categoryName);

}