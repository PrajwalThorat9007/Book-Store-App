package com.bookstore.modules.order.dto;

/*
 * This is the response DTO for the Order module.
 * It carries the full order details returned to the client after placing or fetching an order.
 * The delivery address is flattened into a single string — no raw entity is exposed.
 */

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderResponse {

    // Unique database ID of the order
    private Long id;

    // ID and name of the user who placed the order
    private Long userId;
    private String userName;

    // Delivery address formatted as "line1, city, state pincode"
    private String deliveryAddress;

    // Total cost of the order
    private BigDecimal totalAmount;

    // Current status — PENDING, CONFIRMED, SHIPPED, DELIVERED, or CANCELLED
    private String status;

    // Timestamp of when the order was placed
    private LocalDateTime createdAt;

    // List of individual items in this order
    private List<OrderItemResponse> items;
}
