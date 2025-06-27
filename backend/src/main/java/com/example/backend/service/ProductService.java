package com.example.backend.service;

import com.example.backend.controller.dto.request.ProductWithCategoryDTO;
import com.example.backend.model.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {

    public Product saveProduct(Product product);

    public List<Product> getAllProducts();

    public Boolean deleteProduct(String id);

    public Product getProductById(String id);

    public Product updateProduct(Product product, MultipartFile file);

    public List<Product> searchProduct(String ch);

    public List<Product> findByIsActiveTrue();

    public List<Product> getProductsByCategory(String categoryName);

}