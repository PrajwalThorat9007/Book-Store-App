package com.bookstore.modules.order.controller;

/*
 * This is the controller layer for the Order module.
 * It handles all HTTP requests related to placing, viewing, cancelling, and managing orders.
 * User endpoints require ROLE_USER; admin endpoints require ROLE_ADMIN.
 * The logged-in user's email is extracted from the JWT via @AuthenticationPrincipal.
 * Delegates all business logic to OrderService.
 * Base URL: /api/orders
 */

import com.bookstore.modules.order.dto.OrderRequest;
import com.bookstore.modules.order.dto.OrderResponse;
import com.bookstore.modules.order.dto.OrderStatusUpdateRequest;
import com.bookstore.modules.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // ── USER ENDPOINTS ───────────────────────────────────────────

    // Places a new order from the user's cart — cart must not be empty, address ID is required
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<OrderResponse> placeOrder(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody OrderRequest orderRequest) {

        // Extract email from JWT principal — OrderService uses it to find the User entity
        String email = userDetails.getUsername();
        OrderResponse response = orderService.placeOrder(email, orderRequest);

        // 201 Created is the correct status when a new resource is created
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Returns the full order history of the currently logged-in user — newest first
    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<OrderResponse>> getUserOrders(
            @AuthenticationPrincipal UserDetails userDetails) {

        String email = userDetails.getUsername();
        List<OrderResponse> orders = orderService.getOrdersByUser(email);
        return ResponseEntity.ok(orders);
    }

    // Returns details of a single order — service verifies the order belongs to this user
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<OrderResponse> getOrderById(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id) {

        String email = userDetails.getUsername();
        OrderResponse response = orderService.getOrderById(email, id);
        return ResponseEntity.ok(response);
    }

    // Cancels an order — only allowed if the order is still in PENDING status
    @PutMapping("/{id}/cancel")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<OrderResponse> cancelOrder(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id) {

        String email = userDetails.getUsername();
        OrderResponse response = orderService.cancelOrder(email, id);
        return ResponseEntity.ok(response);
    }

    // ── ADMIN ENDPOINTS ──────────────────────────────────────────

    // Returns every order in the system across all users — admin dashboard use
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        List<OrderResponse> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    // Updates the status of any order — valid values: PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED
    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrderResponse> updateOrderStatus(
            @PathVariable Long id,
            @Valid @RequestBody OrderStatusUpdateRequest orderStatusUpdateRequest) {

        OrderResponse response = orderService.updateOrderStatus(id, orderStatusUpdateRequest);
        return ResponseEntity.ok(response);
    }
}
