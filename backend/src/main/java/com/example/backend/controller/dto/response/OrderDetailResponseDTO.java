package com.example.backend.controller.dto.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDetailResponseDTO {
    private String id;
    private String orderId;
    private LocalDateTime orderDate;
    private String status;
    private String paymentType;
    private Double total;

    private UserDTO user;
    private AddressDTO address;
    private List<ItemDTO> items;

    @Data
    public static class UserDTO {
        private String name;
        private String email;
        private String mobileNumber;
    }

    @Data
    public static class AddressDTO {
        private String firstName;
        private String lastName;
        private String email;
        private String mobileNo;
        private String address;
        private String city;
        private String state;
    }

    @Data
    public static class ItemDTO {
        private String productId;
        private String productName;
        private Integer quantity;
        private Double price;
        private Double total;
    }
}

