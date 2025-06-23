package com.example.backend.controller;

import com.example.backend.model.entity.Category;
import com.example.backend.model.entity.Product;
import com.example.backend.model.entity.User;
import com.example.backend.service.CartService;
import com.example.backend.service.CategoryService;
import com.example.backend.service.ProductService;
import com.example.backend.service.UserService;
import com.example.backend.util.CommonUtil;
import io.micrometer.common.util.StringUtils;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class HomeController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CartService cartService;

    @GetMapping("/user")
    public ResponseEntity<?> getUserDetails(Principal principal) {
        if (principal != null) {
            String email = principal.getName();
            User user = userService.getUserByEmail(email);
            Integer countCart = cartService.getCountCart(user.getId());
            Map<String, Object> response = new HashMap<>();
            response.put("user", user);
            response.put("countCart", countCart);
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not logged in");
    }

    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllActiveCategory());
    }

    @GetMapping("/products")
    public ResponseEntity<?> getProducts(
            @RequestParam(defaultValue = "") String category,
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "12") int pageSize,
            @RequestParam(defaultValue = "") String ch
    ) {
        Page<Product> page = StringUtils.isEmpty(ch)
                ? productService.getAllActiveProductPagination(pageNo, pageSize, category)
                : productService.searchActiveProductPagination(pageNo, pageSize, category, ch);

        Map<String, Object> response = new HashMap<>();
        response.put("products", page.getContent());
        response.put("pageNo", page.getNumber());
        response.put("totalPages", page.getTotalPages());
        response.put("totalElements", page.getTotalElements());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable int id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @PostMapping("/user")
    public ResponseEntity<?> saveUser(
            @ModelAttribute User user,
            @RequestParam("img") MultipartFile file
    ) throws IOException {

        if (userService.existsEmail(user.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already exists");
        }

        String imageName = file.isEmpty() ? "default.jpg" : file.getOriginalFilename();
        user.setProfileImage(imageName);
        User savedUser = userService.saveUser(user);

        if (!file.isEmpty()) {
            File saveFile = new ClassPathResource("static/img").getFile();
            Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "profile_img" + File.separator + imageName);
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        }

        return ResponseEntity.ok(savedUser);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(
            @RequestParam String email,
            HttpServletRequest request
    ) {
        User user = userService.getUserByEmail(email);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email not found");
        }

        String token = UUID.randomUUID().toString();
        userService.updateUserResetToken(email, token);

        String resetUrl = CommonUtil.generateUrl(request) + "/reset-password?token=" + token;

        try {
            boolean emailSent = commonUtil.sendMail(resetUrl, email);
            if (!emailSent) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send email");
            }
            return ResponseEntity.ok("Password reset email sent");
        } catch (UnsupportedEncodingException | MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error sending email: " + e.getMessage());
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(
            @RequestParam String token,
            @RequestParam String password
    ) {
        User user = userService.getUserByToken(token);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired token");
        }

        user.setPassword(passwordEncoder.encode(password));
        user.setResetToken(null);
        userService.updateUser(user);
        return ResponseEntity.ok("Password reset successful");
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchProduct(@RequestParam String ch) {
        List<Product> products = productService.searchProduct(ch);
        return ResponseEntity.ok(products);
    }
}
