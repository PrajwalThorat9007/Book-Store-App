// Manas: Module changed → modules/admin/controller/AdminController.java
// What's changed: Was an empty TODO stub, now fully implemented with all 7 admin endpoints
// Dashboard, users, orders, status update, delete user, revenue, top products

package com.bookstore.modules.admin.controller;

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

    // Manas: RequiredArgsConstructor injects AdminService — no @Autowired needed
    private final AdminService adminService;

    // Manas: GET /api/admin/dashboard?email=admin@test.com
    // Returns totalUsers, totalOrders, totalProducts, totalRevenue, pendingOrders, lowStockProducts
    @GetMapping("/dashboard")
    public ResponseEntity<DashboardResponse> getDashboard(@RequestParam String email) {
        DashboardResponse stats = adminService.getDashboardStats(email);
        return ResponseEntity.ok(stats);
    }

    // Manas: GET /api/admin/users?email=admin@test.com
    // Returns all users with their order count and total spend
    @GetMapping("/users")
    public ResponseEntity<List<UserListResponse>> getAllUsers(@RequestParam String email) {
        List<UserListResponse> users = adminService.getAllUsers(email);
        return ResponseEntity.ok(users);
    }

    // Manas: GET /api/admin/orders?email=admin@test.com
    // Returns all orders with user info, status, amount and item count
    @GetMapping("/orders")
    public ResponseEntity<List<OrderSummary>> getAllOrders(@RequestParam String email) {
        List<OrderSummary> orders = adminService.getAllOrders(email);
        return ResponseEntity.ok(orders);
    }

    // Manas: PUT /api/admin/orders/{id}/status?email=admin@test.com&status=SHIPPED
    // Admin manually moves an order through the status flow
    @PutMapping("/orders/{id}/status")
    public ResponseEntity<OrderSummary> updateOrderStatus(
            @PathVariable Long id,
            @RequestParam String status,
            @RequestParam String email) {
        OrderSummary updated = adminService.updateOrderStatus(id, status, email);
        return ResponseEntity.ok(updated);
    }

    // Manas: DELETE /api/admin/users/{id}?email=admin@test.com
    // Hard deletes a user — admin only, irreversible
    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(
            @PathVariable Long id,
            @RequestParam String email) {
        adminService.deleteUser(id, email);
        return ResponseEntity.ok("User " + id + " deleted successfully");
    }

    // Manas: GET /api/admin/analytics/revenue?email=admin@test.com
    // Returns total revenue as a single BigDecimal number
    @GetMapping("/analytics/revenue")
    public ResponseEntity<BigDecimal> getTotalRevenue(@RequestParam String email) {
        BigDecimal revenue = adminService.getTotalRevenue(email);
        return ResponseEntity.ok(revenue);
    }

    // Manas: GET /api/admin/analytics/top-products?email=admin@test.com
    // Returns products sorted by units sold — best sellers first
    @GetMapping("/analytics/top-products")
    public ResponseEntity<List<Map.Entry<String, Integer>>> getTopProducts(
            @RequestParam String email) {
        List<Map.Entry<String, Integer>> topProducts = adminService.getTopSellingProducts(email);
        return ResponseEntity.ok(topProducts);
    }
}