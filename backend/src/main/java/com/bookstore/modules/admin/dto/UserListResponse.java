// Manas: Module changed → modules/admin/dto/UserListResponse.java
// What's changed: Was an empty TODO stub, now implemented with all user summary fields
// Admin needs to see user info in a list — no password field ever goes in here

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
public class UserListResponse {

    private Long id;
    private String name;
    private String email;

    // Manas: role will be USER or ADMIN — frontend can use this to highlight admin accounts
    private String role;

    private LocalDateTime createdAt;

    // Manas: These two give the admin a sense of how active/valuable each user is
    private int totalOrders;
    private BigDecimal totalSpent;
}