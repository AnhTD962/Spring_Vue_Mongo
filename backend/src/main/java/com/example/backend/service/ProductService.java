package com.example.backend.service;

import com.example.backend.model.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {

    Product saveProduct(Product product, MultipartFile image);

    Product getProductById(String id);

    Page<Product> findByIsActiveTrue(Pageable pageable);

    Page<Product> getProductsByCategory(String categoryName, Pageable pageable);

    Product updateProductById(String id, Product product, MultipartFile image);

    Page<Product> getProducts(String searchTerm, Pageable pageable);

    Product getProductOrThrow(String id);

    String deleteProductOrThrow(String id);

}