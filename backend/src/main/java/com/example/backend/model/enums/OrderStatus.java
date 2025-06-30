package com.example.backend.model.enums;

public enum OrderStatus {

    IN_PROGRESS(1, "In Progress"),
    ORDER_RECEIVED(2, "Order Received"),
    PRODUCT_PACKED(3, "Product Packed"),
    OUT_FOR_DELIVERY(4, "Out for Delivery"),
    DELIVERED(5, "Delivered"),
    CANCELLED(6, "Cancelled"),
    SUCCESS(7, "Success");

    private final int id;
    private final String name;

    OrderStatus(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public static OrderStatus fromString(String status) {
        if (status == null || status.trim().isEmpty()) {
            throw new IllegalArgumentException("Status cannot be null or empty");
        }

        String input = status.trim().toLowerCase();

        for (OrderStatus os : OrderStatus.values()) {
            if (os.getName().toLowerCase().equals(input)) {
                return os;
            }
        }

        throw new IllegalArgumentException("Invalid order status: " + status);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
