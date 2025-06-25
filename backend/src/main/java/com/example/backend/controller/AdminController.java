package com.example.backend.controller;

import com.example.backend.model.entity.Category;
import com.example.backend.model.entity.Order;
import com.example.backend.model.entity.Product;
import com.example.backend.model.entity.User;
import com.example.backend.model.enums.OrderStatus;
import com.example.backend.service.*;
import com.example.backend.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ROLE_ADMIN')")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class AdminController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategory());
    }

    @PostMapping("/categories")
    public ResponseEntity<?> saveCategory(
            @ModelAttribute Category category,
            @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {

        if (category.getName() == null || category.getName().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Category name is required");
        }

        if (categoryService.existCategory(category.getName())) {
            return ResponseEntity.badRequest().body("Category name already exists");
        }

        String imageName = (file != null && !file.isEmpty()) ? file.getOriginalFilename() : "default.jpg";
        category.setImageName(imageName);

        if (category.getIsActive() == null) {
            category.setIsActive(true);
        }

        Category saved = categoryService.saveCategory(category);

        // Lưu file ảnh vào thư mục uploads/category_img/
        if (file != null && !file.isEmpty()) {
            Path uploadDir = Paths.get("uploads/category_img/");
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            Path path = uploadDir.resolve(file.getOriginalFilename());
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        }

        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/categories/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable String id) {
        boolean deleted = categoryService.deleteCategory(id);
        if (deleted) {
            return ResponseEntity.ok("Category deleted successfully");
        } else {
            return ResponseEntity.internalServerError().body("Something went wrong");
        }
    }

    @PutMapping("/categories/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable String id,
                                            @ModelAttribute Category category,
                                            @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
        Category existing = categoryService.getCategoryById(id);
        if (existing == null) return ResponseEntity.notFound().build();

        String imageName = (file != null && !file.isEmpty()) ? file.getOriginalFilename() : existing.getImageName();

        existing.setName(category.getName());
        existing.setIsActive(category.getIsActive());
        existing.setImageName(imageName);

        Category updated = categoryService.saveCategory(existing);

        if (file != null && !file.isEmpty()) {
            Path uploadDir = Paths.get("uploads/category_img/");
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            Path path = uploadDir.resolve(file.getOriginalFilename());
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        }

        return ResponseEntity.ok(updated);
    }

    @PostMapping("/products")
    public ResponseEntity<?> saveProduct(@ModelAttribute Product product,
                                         @RequestParam(value = "file", required = false) MultipartFile image) throws IOException {
        if (product.getDiscount() < 0 || product.getDiscount() > 100) {
            return ResponseEntity.badRequest().body("Invalid discount value");
        }

        // Tính toán giá sau giảm
        double discountAmount = product.getPrice() * (product.getDiscount() / 100.0);
        double discountedPrice = product.getPrice() - discountAmount;
        product.setDiscountPrice(discountedPrice);

        // Handle the image file
        if (image != null && !image.isEmpty()) {
            String imageName = image.getOriginalFilename();
            product.setImage(imageName);

            // Tạo thư mục nếu chưa tồn tại
            Path uploadDir = Paths.get("uploads/product_img/");
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            // Lưu ảnh
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

    @GetMapping("/products")
    public ResponseEntity<Page<Product>> getProducts(@RequestParam(defaultValue = "") String ch,
                                                     @RequestParam(defaultValue = "0") Integer pageNo,
                                                     @RequestParam(defaultValue = "10") Integer pageSize) {
        Page<Product> page;
        if (ch != null && !ch.trim().isEmpty()) {
            page = productService.searchProductPagination(pageNo, pageSize, ch);
        } else {
            page = productService.getAllProductsPagination(pageNo, pageSize);
        }
        return ResponseEntity.ok(page);
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

        if (product.getDiscount() < 0 || product.getDiscount() > 100) {
            return ResponseEntity.badRequest().body("Invalid discount value");
        }

        // Lấy sản phẩm hiện tại từ DB
        Product existingProduct = productService.getProductById(id);
        if (existingProduct == null) {
            return ResponseEntity.notFound().build();
        }

        // Cập nhật thông tin
        existingProduct.setTitle(product.getTitle());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setCategory(product.getCategory());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setStock(product.getStock());
        existingProduct.setDiscount(product.getDiscount());
        existingProduct.setIsActive(product.getIsActive());

        // Tính lại discount price
        double discountAmount = product.getPrice() * (product.getDiscount() / 100.0);
        existingProduct.setDiscountPrice(product.getPrice() - discountAmount);

        // Nếu có ảnh mới
        if (image != null && !image.isEmpty()) {
            String imageName = image.getOriginalFilename();
            existingProduct.setImage(imageName);

            // Lưu ảnh vào thư mục ngoài project
            Path uploadDir = Paths.get("uploads/product_img/");
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            Path path = uploadDir.resolve(imageName);
            Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        }

        Product updatedProduct = productService.updateProduct(existingProduct, image);
        if (updatedProduct != null) {
            return ResponseEntity.ok(updatedProduct);
        } else {
            return ResponseEntity.internalServerError().body("Something went wrong");
        }
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers(@RequestParam(value = "type", required = false) Integer type) {
        List<User> users;

        if (type == null) {
            users = userService.getAllUsers(); // Ví dụ: lấy toàn bộ users
        } else if (type == 1) {
            users = userService.getUsers("ROLE_USER");
        } else {
            users = userService.getUsers("ROLE_ADMIN");
        }

        return ResponseEntity.ok(users);
    }

    @PutMapping("/users/{id}/status")
    public ResponseEntity<String> updateUserAccountStatus(@PathVariable String id, @RequestParam Boolean status) {
        boolean updated = userService.updateAccountStatus(id, status);
        if (updated) {
            return ResponseEntity.ok("Account Status Updated");
        } else {
            return ResponseEntity.internalServerError().body("Something went wrong on server");
        }
    }

    @GetMapping("/orders")
    public ResponseEntity<Page<Order>> getAllOrders(@RequestParam(name = "pageNo", defaultValue = "0") Integer pageNo,
                                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Page<Order> page = orderService.getAllOrdersPagination(pageNo, pageSize);
        return ResponseEntity.ok(page);
    }

    @PutMapping("/orders/{id}/status")
    public ResponseEntity<String> updateOrderStatus(@PathVariable String id, @RequestParam Integer st) {
        OrderStatus[] values = OrderStatus.values();
        String status = null;
        for (OrderStatus orderSt : values) {
            if (orderSt.getId().equals(st)) {
                status = orderSt.getName();
                break;
            }
        }

        if (status == null) {
            return ResponseEntity.badRequest().body("Invalid status value");
        }

        Order updateOrder = orderService.updateOrderStatus(id, status);

        try {
            commonUtil.sendMailForProductOrder(updateOrder, status);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!ObjectUtils.isEmpty(updateOrder)) {
            return ResponseEntity.ok("Status Updated");
        } else {
            return ResponseEntity.internalServerError().body("Status not updated");
        }
    }

    @GetMapping("/orders/search")
    public ResponseEntity<?> searchOrder(@RequestParam String orderId) {
        if (orderId == null || orderId.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Order ID is required");
        }

        Order order = orderService.getOrdersByOrderId(orderId.trim());
        if (ObjectUtils.isEmpty(order)) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(order);
        }
    }

    @PostMapping("/admins")
    public ResponseEntity<String> saveAdmin(@ModelAttribute User user, @RequestParam("img") MultipartFile file) throws IOException {
        String imageName = file.isEmpty() ? "default.jpg" : file.getOriginalFilename();
        user.setProfileImage(imageName);
        User saveUser = userService.saveAdmin(user);

        if (!ObjectUtils.isEmpty(saveUser)) {
            if (!file.isEmpty()) {
                File saveFile = new ClassPathResource("static/img").getFile();
                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "profile_img" + File.separator + file.getOriginalFilename());
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            }
            return ResponseEntity.ok("Register successfully");
        } else {
            return ResponseEntity.internalServerError().body("Something went wrong on server");
        }
    }

    @PutMapping("/profile")
    public ResponseEntity<String> updateProfile(@ModelAttribute User user, @RequestParam MultipartFile img) {
        User updateUserProfile = userService.updateUserProfile(user, img);
        if (ObjectUtils.isEmpty(updateUserProfile)) {
            return ResponseEntity.internalServerError().body("Profile not updated");
        } else {
            return ResponseEntity.ok("Profile Updated");
        }
    }

    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestParam String newPassword,
                                                 @RequestParam String currentPassword,
                                                 Principal p) {
        User loggedInUserDetails = commonUtil.getLoggedInUserDetails(p);
        boolean matches = passwordEncoder.matches(currentPassword, loggedInUserDetails.getPassword());

        if (matches) {
            String encodePassword = passwordEncoder.encode(newPassword);
            loggedInUserDetails.setPassword(encodePassword);
            User updateUser = userService.updateUser(loggedInUserDetails);

            if (ObjectUtils.isEmpty(updateUser)) {
                return ResponseEntity.internalServerError().body("Password not updated !! Error in server");
            } else {
                return ResponseEntity.ok("Password Updated successfully");
            }
        } else {
            return ResponseEntity.badRequest().body("Current Password incorrect");
        }
    }
}
