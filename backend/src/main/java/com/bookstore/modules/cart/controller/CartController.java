package com.bookstore.modules.cart.controller;

/*
 * This is the controller layer for the Cart module.
 * It handles all HTTP requests related to the shopping cart — viewing, adding, updating, and clearing items.
 * Delegates all business logic to CartService.
 * Base URL: /api/cart
 */

import com.bookstore.common.ApiResponse;
import com.bookstore.modules.cart.dto.*;
import com.bookstore.modules.cart.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    // Returns the current user's cart — creates an empty cart if one doesn't exist yet
    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ApiResponse<CartResponse>> getCart() {
        return ResponseEntity.ok(ApiResponse.success(cartService.getCart()));
    }

    // Adds a product to the cart — merges quantity if the product is already in the cart
    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ApiResponse<CartResponse>> addToCart(@Valid @RequestBody AddToCartRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Item added to cart", cartService.addToCart(request)));
    }

    // Updates the quantity of a specific product already in the cart
    @PutMapping("/update/{productId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ApiResponse<CartResponse>> updateQuantity(
            @PathVariable Long productId,
            @Valid @RequestBody UpdateCartItemRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Cart updated", cartService.updateQuantity(productId, request)));
    }

    // Removes a specific product from the cart entirely
    @DeleteMapping("/remove/{productId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ApiResponse<CartResponse>> removeItem(@PathVariable Long productId) {
        return ResponseEntity.ok(ApiResponse.success("Item removed", cartService.removeItem(productId)));
    }

    // Clears all items from the cart and resets the total to zero
    @DeleteMapping("/clear")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ApiResponse<Void>> clearCart() {
        cartService.clearCart();
        return ResponseEntity.ok(ApiResponse.success("Cart cleared", null));
    }
}
