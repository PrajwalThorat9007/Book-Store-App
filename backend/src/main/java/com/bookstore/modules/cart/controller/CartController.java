package com.bookstore.modules.cart.controller;

import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for Cart operations
 * 
 * TODO: Implement the following endpoints:
 * - GET    /api/cart              - Get current user's cart
 * - POST   /api/cart/add          - Add item to cart
 * - PUT    /api/cart/items/{id}   - Update cart item quantity
 * - DELETE /api/cart/items/{id}   - Remove item from cart
 * - DELETE /api/cart/clear        - Clear entire cart
 * 
 * TODO: Add security:
 * - @PreAuthorize("hasRole('USER')") on all methods
 * - Get userId from SecurityContext
 * 
 * TODO: Add validation:
 * - @Valid on request bodies
 * - Handle validation errors
 * 
 * TODO: Return proper HTTP status codes:
 * - 200 OK for successful operations
 * - 201 Created for add to cart
 * - 204 No Content for delete operations
 * - 400 Bad Request for validation errors
 * - 404 Not Found if cart/item doesn't exist
 */
@RestController
@RequestMapping("/api/cart")
public class CartController {
    // TODO: Implement cart controller endpoints
}
