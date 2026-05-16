// Manas: Module changed → modules/admin/service/AdminService.java
// What's changed: Was an empty TODO stub, now fully implemented with dashboard stats,
// user list, order list, order status update, delete user, revenue analytics, and top products
// Fixed type inference issue in getTopSellingProducts by explicitly typing OrderItem in lambdas
// Fixed getItems() → getOrderItems() to match the actual field name in Order entity

package com.bookstore.modules.admin.service;

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

    // Manas: No admin repository needed — admin just reads from existing module repositories
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    // Manas: Every public method calls this first — if caller is not ADMIN we stop immediately
    private User verifyAdmin(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("No user found with email: " + email));

        if (!user.getRole().equals("ADMIN")) {
            log.warn("Blocked non-admin access attempt from: {}", email);
            throw new RuntimeException("Access denied. User is not an admin: " + email);
        }

        return user;
    }

    // Manas: Dashboard — aggregates all the top-level numbers in one shot
    @Transactional(readOnly = true)
    public DashboardResponse getDashboardStats(String adminEmail) {
        verifyAdmin(adminEmail);

        long totalUsers    = userRepository.count();
        long totalOrders   = orderRepository.count();
        long totalProducts = productRepository.count();

        // Manas: Summing totalAmount across every order for the revenue figure
        BigDecimal totalRevenue = orderRepository.findAll().stream()
                .map(Order::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Manas: Pending orders — admin needs to know what still needs processing
        long pendingOrders = orderRepository.findAll().stream()
                .filter(o -> o.getStatus().equals("PENDING"))
                .count();

        // Manas: Low stock = products with stockQuantity below 10
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

    // Manas: Returns all users with their order count and total spend calculated on the fly
    @Transactional(readOnly = true)
    public List<UserListResponse> getAllUsers(String adminEmail) {
        verifyAdmin(adminEmail);

        log.info("Admin {} fetching all users", adminEmail);

        List<Order> allOrders = orderRepository.findAll();

        return userRepository.findAll().stream()
                .map(u -> {
                    // Manas: Filter orders belonging to this specific user
                    List<Order> userOrders = allOrders.stream()
                            .filter(o -> o.getUser().getId().equals(u.getId()))
                            .collect(Collectors.toList());

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

    // Manas: Returns all orders with user info embedded for the admin order management table
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
                        .itemCount(o.getOrderItems() != null ? o.getOrderItems().size() : 0)
                        .build())
                .collect(Collectors.toList());
    }

    // Manas: Admin manually updates order status — e.g. PENDING → CONFIRMED, SHIPPED → DELIVERED
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

    // Manas: Hard delete a user by ID — admin only, use carefully
    @Transactional
    public void deleteUser(Long userId, String adminEmail) {
        verifyAdmin(adminEmail);

        User target = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        userRepository.delete(target);
        log.info("Admin {} deleted user {} ({})", adminEmail, userId, target.getEmail());
    }

    // Manas: Standalone revenue endpoint — same logic as dashboard but returns just the number
    @Transactional(readOnly = true)
    public BigDecimal getTotalRevenue(String adminEmail) {
        verifyAdmin(adminEmail);

        BigDecimal revenue = orderRepository.findAll().stream()
                .map(Order::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        log.info("Admin {} fetched total revenue: {}", adminEmail, revenue);
        return revenue;
    }

    // Manas: Groups order items by product title, sums quantities, sorts best seller first
    // Explicitly typed OrderItem in lambdas to fix Java type inference issue across flatMap chain
    @Transactional(readOnly = true)
    public List<Map.Entry<String, Integer>> getTopSellingProducts(String adminEmail) {
        verifyAdmin(adminEmail);

        log.info("Admin {} fetching top selling products", adminEmail);

        Map<String, Integer> salesMap = orderRepository.findAll().stream()
                .flatMap(o -> o.getOrderItems().stream())
                .collect(Collectors.groupingBy(
                        (OrderItem item) -> item.getProduct().getTitle(),
                        Collectors.summingInt((OrderItem item) -> item.getQuantity())
                ));

        return salesMap.entrySet().stream()
                .sorted((a, b) -> b.getValue() - a.getValue())
                .collect(Collectors.toList());
    }
}