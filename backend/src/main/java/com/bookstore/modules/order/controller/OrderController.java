package com.bookstore.modules.order.controller;

import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for Order operations
 * 
 * TODO: Implement the following endpoints:
 * 
 * USER endpoints:
 * - POST   /api/orders              - Place new order from cart
 * - GET    /api/orders              - Get user's order history
 * - GET    /api/orders/{id}         - Get specific order details
 * - PUT    /api/orders/{id}/cancel  - Cancel order (only if PENDING)
 * 
 * ADMIN endpoints:
 * - GET    /api/orders/all          - Get all orders
 * - PUT    /api/orders/{id}/status  - Update order status
 * 
 * TODO: Add security:
 * - @PreAuthorize("hasRole('USER')") on user endpoints
 * - @PreAuthorize("hasRole('ADMIN')") on admin endpoints
 * - Get userId from SecurityContext
 * 
 * TODO: Add validation:
 * - @Valid on request bodies
 * 
 * TODO: Return proper HTTP status codes:
 * - 201 Created for place order
 * - 200 OK for get operations
 * - 400 Bad Request if cart is empty or validation fails
 * - 403 Forbidden if trying to cancel non-PENDING order
 */
@RestController
@RequestMapping("/api/orders")
public class OrderController {
    // TODO: Implement order controller endpoints
}
