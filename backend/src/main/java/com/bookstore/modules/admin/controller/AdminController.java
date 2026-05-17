package com.bookstore.modules.admin.controller;

/*
 * This is the controller layer for the Admin module.
 * It handles all HTTP requests for admin-only operations like dashboard stats,
 * user management, order management, and sales analytics.
 * Uses ?email= query param as temporary auth until JWT role-based security is fully wired up.
 * Delegates all business logic to AdminService.
 * Base URL: /api/admin
 */

import com.bookstore.modules.admin.dto.DashboardResponse;
import com.bookstore.modules.admin.dto.OrderSummary;
import com.bookstore.modules.admin.dto.UserListResponse;
import com.bookstore.modules.admin.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    // Returns dashboard summary — total users, orders, products, revenue, pending orders, low stock count
    @GetMapping("/dashboard")
    public ResponseEntity<DashboardResponse> getDashboard(@RequestParam String email) {
        DashboardResponse stats = adminService.getDashboardStats(email);
        return ResponseEntity.ok(stats);
    }

    // Returns all registered users with their order count and total spend
    @GetMapping("/users")
    public ResponseEntity<List<UserListResponse>> getAllUsers(@RequestParam String email) {
        List<UserListResponse> users = adminService.getAllUsers(email);
        return ResponseEntity.ok(users);
    }

    // Returns all orders in the system with user info, status, amount, and item count
    @GetMapping("/orders")
    public ResponseEntity<List<OrderSummary>> getAllOrders(@RequestParam String email) {
        List<OrderSummary> orders = adminService.getAllOrders(email);
        return ResponseEntity.ok(orders);
    }

    // Manually moves an order through the status flow — e.g. PENDING → CONFIRMED → SHIPPED
    @PutMapping("/orders/{id}/status")
    public ResponseEntity<OrderSummary> updateOrderStatus(
            @PathVariable Long id,
            @RequestParam String status,
            @RequestParam String email) {
        OrderSummary updated = adminService.updateOrderStatus(id, status, email);
        return ResponseEntity.ok(updated);
    }

    // Hard deletes a user by ID — irreversible, admin use only
    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(
            @PathVariable Long id,
            @RequestParam String email) {
        adminService.deleteUser(id, email);
        return ResponseEntity.ok("User " + id + " deleted successfully");
    }

    // Returns the total revenue across all orders as a single BigDecimal value
    @GetMapping("/analytics/revenue")
    public ResponseEntity<BigDecimal> getTotalRevenue(@RequestParam String email) {
        BigDecimal revenue = adminService.getTotalRevenue(email);
        return ResponseEntity.ok(revenue);
    }

    // Returns products sorted by total units sold — best sellers appear first
    @GetMapping("/analytics/top-products")
    public ResponseEntity<List<Map.Entry<String, Integer>>> getTopProducts(
            @RequestParam String email) {
        List<Map.Entry<String, Integer>> topProducts = adminService.getTopSellingProducts(email);
        return ResponseEntity.ok(topProducts);
    }
}
