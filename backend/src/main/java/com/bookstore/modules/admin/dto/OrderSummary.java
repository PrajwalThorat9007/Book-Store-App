package com.bookstore.modules.admin.dto;

/*
 * This is the response DTO for order summaries in the Admin module.
 * It provides a flat view of an order with embedded user info so the admin order table
 * doesn't need extra API calls to display who placed each order.
 */

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderSummary {

    // Unique ID of the order
    private Long orderId;

    // User who placed the order — embedded directly to avoid extra API calls from the frontend
    private Long userId;
    private String userName;
    private String userEmail;

    // Current order status — PENDING, CONFIRMED, SHIPPED, DELIVERED, or CANCELLED
    private String status;

    // Total cost of the order
    private BigDecimal totalAmount;

    // Timestamp of when the order was placed
    private LocalDateTime createdAt;

    // Number of items in the order — gives a quick sense of order size without loading all items
    private int itemCount;
}
