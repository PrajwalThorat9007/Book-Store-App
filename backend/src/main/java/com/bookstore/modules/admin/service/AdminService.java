package com.bookstore.modules.admin.service;

/*
 * This is the service layer class for the Admin module.
 * It contains all business logic for admin dashboard, user management, order management, and analytics.
 * Every public method first calls verifyAdmin() to ensure only ADMIN role users can proceed.
 * Does not have its own repository — reads data from existing module repositories (user, order, product).
 */

import com.bookstore.entity.Order;
import com.bookstore.entity.OrderItem;
import com.bookstore.entity.User;
import com.bookstore.modules.admin.dto.DashboardResponse;
import com.bookstore.modules.admin.dto.OrderSummary;
import com.bookstore.modules.admin.dto.UserListResponse;
import com.bookstore.modules.order.repository.OrderRepository;
import com.bookstore.modules.product.repository.ProductRepository;
import com.bookstore.modules.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    // Verifies the caller is an ADMIN — called at the start of every public method to block non-admins
    private User verifyAdmin(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("No user found with email: " + email));

        if (!user.getRole().equals("ADMIN")) {
            log.warn("Blocked non-admin access attempt from: {}", email);
            throw new RuntimeException("Access denied. User is not an admin: " + email);
        }

        return user;
    }

    // Aggregates all top-level stats for the admin dashboard in a single call
    @Transactional(readOnly = true)
    public DashboardResponse getDashboardStats(String adminEmail) {
        verifyAdmin(adminEmail);

        long totalUsers    = userRepository.count();
        long totalOrders   = orderRepository.count();
        long totalProducts = productRepository.count();

        // Sum totalAmount across all orders to get overall revenue
        BigDecimal totalRevenue = orderRepository.findAll().stream()
                .map(Order::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Count orders still in PENDING state — these need admin attention
        long pendingOrders = orderRepository.findAll().stream()
                .filter(o -> o.getStatus().equals("PENDING"))
                .count();

        // Flag products with stock below 10 as low stock
        long lowStockProducts = productRepository.findAll().stream()
                .filter(p -> p.getStockQuantity() < 10)
                .count();

        log.info("Admin {} fetched dashboard: users={}, orders={}, revenue={}, pending={}, lowStock={}",
                adminEmail, totalUsers, totalOrders, totalRevenue, pendingOrders, lowStockProducts);

        return DashboardResponse.builder()
                .totalUsers(totalUsers)
                .totalOrders(totalOrders)
                .totalProducts(totalProducts)
                .totalRevenue(totalRevenue)
                .pendingOrders(pendingOrders)
                .lowStockProducts(lowStockProducts)
                .build();
    }

    // Returns all users with their order count and total spend calculated on the fly
    @Transactional(readOnly = true)
    public List<UserListResponse> getAllUsers(String adminEmail) {
        verifyAdmin(adminEmail);
        log.info("Admin {} fetching all users", adminEmail);

        List<Order> allOrders = orderRepository.findAll();

        return userRepository.findAll().stream()
                .map(u -> {
                    // Filter orders that belong to this specific user
                    List<Order> userOrders = allOrders.stream()
                            .filter(o -> o.getUser().getId().equals(u.getId()))
                            .collect(Collectors.toList());

                    // Sum all order amounts to get the user's total spend
                    BigDecimal totalSpent = userOrders.stream()
                            .map(Order::getTotalAmount)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    return UserListResponse.builder()
                            .id(u.getId())
                            .name(u.getName())
                            .email(u.getEmail())
                            .role(u.getRole())
                            .createdAt(u.getCreatedAt())
                            .totalOrders(userOrders.size())
                            .totalSpent(totalSpent)
                            .build();
                })
                .collect(Collectors.toList());
    }

    // Returns all orders with embedded user info — used in the admin order management table
    @Transactional(readOnly = true)
    public List<OrderSummary> getAllOrders(String adminEmail) {
        verifyAdmin(adminEmail);
        log.info("Admin {} fetching all orders", adminEmail);

        return orderRepository.findAll().stream()
                .map(o -> OrderSummary.builder()
                        .orderId(o.getId())
                        .userId(o.getUser().getId())
                        .userName(o.getUser().getName())
                        .userEmail(o.getUser().getEmail())
                        .status(o.getStatus())
                        .totalAmount(o.getTotalAmount())
                        .createdAt(o.getCreatedAt())
                        // itemCount gives a quick sense of order size without loading all items
                        .itemCount(o.getOrderItems() != null ? o.getOrderItems().size() : 0)
                        .build())
                .collect(Collectors.toList());
    }

    // Updates the status of any order — admin can move orders through any status transition
    @Transactional
    public OrderSummary updateOrderStatus(Long orderId, String newStatus, String adminEmail) {
        verifyAdmin(adminEmail);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));

        String oldStatus = order.getStatus();
        order.setStatus(newStatus);
        orderRepository.save(order);

        log.info("Admin {} changed order {} status: {} → {}", adminEmail, orderId, oldStatus, newStatus);

        return OrderSummary.builder()
                .orderId(order.getId())
                .userId(order.getUser().getId())
                .userName(order.getUser().getName())
                .userEmail(order.getUser().getEmail())
                .status(order.getStatus())
                .totalAmount(order.getTotalAmount())
                .createdAt(order.getCreatedAt())
                .itemCount(order.getOrderItems() != null ? order.getOrderItems().size() : 0)
                .build();
    }

    // Hard deletes a user by ID — use carefully, this is irreversible
    @Transactional
    public void deleteUser(Long userId, String adminEmail) {
        verifyAdmin(adminEmail);

        User target = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        userRepository.delete(target);
        log.info("Admin {} deleted user {} ({})", adminEmail, userId, target.getEmail());
    }

    // Returns total revenue by summing all order amounts — same logic as dashboard but standalone
    @Transactional(readOnly = true)
    public BigDecimal getTotalRevenue(String adminEmail) {
        verifyAdmin(adminEmail);

        BigDecimal revenue = orderRepository.findAll().stream()
                .map(Order::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        log.info("Admin {} fetched total revenue: {}", adminEmail, revenue);
        return revenue;
    }

    // Groups all order items by product title, sums quantities, and sorts best sellers first
    @Transactional(readOnly = true)
    public List<Map.Entry<String, Integer>> getTopSellingProducts(String adminEmail) {
        verifyAdmin(adminEmail);
        log.info("Admin {} fetching top selling products", adminEmail);

        // Flatten all order items across all orders, group by product title, sum quantities
        Map<String, Integer> salesMap = orderRepository.findAll().stream()
                .flatMap(o -> o.getOrderItems().stream())
                .collect(Collectors.groupingBy(
                        (OrderItem item) -> item.getProduct().getTitle(),
                        Collectors.summingInt((OrderItem item) -> item.getQuantity())
                ));

        // Sort descending by quantity sold — highest selling product comes first
        return salesMap.entrySet().stream()
                .sorted((a, b) -> b.getValue() - a.getValue())
                .collect(Collectors.toList());
    }
}
