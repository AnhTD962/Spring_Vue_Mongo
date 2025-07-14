package com.example.backend.service;

import com.example.backend.model.entity.Product;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {

    Product saveProduct(Product product, MultipartFile image);

    Product getProductById(String id);

    List<Product> findByIsActiveTrue();

    List<Product> getProductsByCategory(String categoryName);

    Product updateProductById(String id, Product product, MultipartFile image);

    List<Product> getProducts(String searchTerm);

    Product getProductOrThrow(String id);

    String deleteProductOrThrow(String id);

}