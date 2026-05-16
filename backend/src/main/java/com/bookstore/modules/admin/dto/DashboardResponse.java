// Manas: Module changed → modules/admin/dto/DashboardResponse.java
// What's changed: Was an empty TODO stub, now implemented with all dashboard stats fields
// Kept the original filename since the stub already had this name in the project structure

package com.bookstore.modules.admin.dto;

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

    // Manas: The four main numbers the admin dashboard card section shows
    private long totalUsers;
    private long totalOrders;
    private long totalProducts;

    // Manas: BigDecimal for money — never use double/float for currency
    private BigDecimal totalRevenue;

    // Manas: Extra useful fields the TODO comment suggested — added both
    private long pendingOrders;
    private long lowStockProducts;
}