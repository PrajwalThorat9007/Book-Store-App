package com.bookstore.modules.admin.dto;

/*
 * This is the response DTO for the admin dashboard endpoint.
 * It carries all the top-level summary numbers shown on the admin dashboard page.
 * Built using the builder pattern — AdminService constructs it via DashboardResponse.builder().
 */

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResponse {

    // Total number of registered users in the system
    private long totalUsers;

    // Total number of orders placed across all users
    private long totalOrders;

    // Total number of products listed in the store
    private long totalProducts;

    // Sum of all order amounts — BigDecimal used to avoid floating point precision issues
    private BigDecimal totalRevenue;

    // Number of orders still in PENDING status — needs admin action
    private long pendingOrders;

    // Number of products with stock quantity below 10 — needs restocking
    private long lowStockProducts;
}
