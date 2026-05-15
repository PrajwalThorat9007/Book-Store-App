package com.bookstore.modules.wishlist.controller;

import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for Wishlist operations
 * 
 * TODO: Implement the following endpoints:
 * - GET    /api/wishlist                - Get current user's wishlist
 * - POST   /api/wishlist/add/{productId} - Add product to wishlist
 * - DELETE /api/wishlist/remove/{productId} - Remove product from wishlist
 * - GET    /api/wishlist/check/{productId} - Check if product is in wishlist
 * 
 * TODO: Add security:
 * - @PreAuthorize("hasRole('USER')") on all methods
 * - Get userId from SecurityContext
 * 
 * TODO: Return proper HTTP status codes:
 * - 200 OK for successful operations
 * - 201 Created for add to wishlist
 * - 204 No Content for remove
 * - 409 Conflict if product already in wishlist
 */
@RestController
@RequestMapping("/api/wishlist")
public class WishlistController {
    // TODO: Implement wishlist controller endpoints
}
