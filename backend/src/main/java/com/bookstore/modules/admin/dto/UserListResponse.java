package com.bookstore.modules.admin.dto;

/*
 * This is the response DTO for the user list in the Admin module.
 * It carries a summary of each user shown in the admin user management table.
 * Password is intentionally excluded — never included in any response DTO.
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
public class UserListResponse {

    // Unique database ID of the user
    private Long id;

    // Display name of the user
    private String name;

    // Email address — also the login identifier
    private String email;

    // Role — either USER or ADMIN, used to highlight admin accounts in the UI
    private String role;

    // Timestamp of when the account was created
    private LocalDateTime createdAt;

    // Total number of orders placed by this user — shows how active they are
    private int totalOrders;

    // Sum of all order amounts for this user — shows their total spend
    private BigDecimal totalSpent;
}
