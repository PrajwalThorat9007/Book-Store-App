// Manas: Module changed → modules/admin/dto/OrderSummary.java
// What's changed: New file — admin needs a flat order view with user info embedded
// so the frontend order list table doesn't need extra API calls to get user details

package com.bookstore.modules.admin.dto;

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

    private Long orderId;

    // Manas: Embedding user info directly — admin order table shows who placed the order
    private Long userId;
    private String userName;
    private String userEmail;

    // Manas: status can be PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED
    private String status;

    private BigDecimal totalAmount;
    private LocalDateTime createdAt;

    // Manas: itemCount tells admin how big the order is without sending the full item list
    private int itemCount;
}