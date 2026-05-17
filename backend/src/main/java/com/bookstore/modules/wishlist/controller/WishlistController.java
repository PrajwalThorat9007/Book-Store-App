package com.bookstore.modules.wishlist.controller;

/*
 * This is the controller layer for the Wishlist module.
 * It handles all HTTP requests related to a user's saved product wishlist.
 * Delegates all business logic to WishlistService.
 * Base URL: /api/wishlist
 */

import com.bookstore.modules.wishlist.dto.WishlistResponse;
import com.bookstore.modules.wishlist.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/wishlist")
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistService wishlistService;

    // Returns the current user's wishlist — auto-creates an empty one if it doesn't exist yet
    @GetMapping
    public ResponseEntity<WishlistResponse> getWishlist() {
        return ResponseEntity.ok(wishlistService.getWishlist());
    }

    // Adds a product to the wishlist — returns 409 if the product is already saved
    @PostMapping("/add/{productId}")
    public ResponseEntity<WishlistResponse> addToWishlist(@PathVariable Long productId) {
        WishlistResponse response = wishlistService.addToWishlist(productId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Removes a product from the wishlist — returns 204 No Content on success
    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<Void> removeFromWishlist(@PathVariable Long productId) {
        wishlistService.removeFromWishlist(productId);
        return ResponseEntity.noContent().build();
    }

    // Checks if a product is already in the wishlist — returns { "inWishlist": true/false }
    @GetMapping("/check/{productId}")
    public ResponseEntity<Map<String, Boolean>> isInWishlist(@PathVariable Long productId) {
        boolean inWishlist = wishlistService.isInWishlist(productId);
        return ResponseEntity.ok(Map.of("inWishlist", inWishlist));
    }
}
