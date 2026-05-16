package com.bookstore.modules.cart.controller;

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

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ApiResponse<CartResponse>> getCart() {
        return ResponseEntity.ok(ApiResponse.success(cartService.getCart()));
    }

    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ApiResponse<CartResponse>> addToCart(@Valid @RequestBody AddToCartRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Item added to cart", cartService.addToCart(request)));
    }

    @PutMapping("/update/{productId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ApiResponse<CartResponse>> updateQuantity(
            @PathVariable Long productId,
            @Valid @RequestBody UpdateCartItemRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Cart updated", cartService.updateQuantity(productId, request)));
    }

    @DeleteMapping("/remove/{productId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ApiResponse<CartResponse>> removeItem(@PathVariable Long productId) {
        return ResponseEntity.ok(ApiResponse.success("Item removed", cartService.removeItem(productId)));
    }

    @DeleteMapping("/clear")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ApiResponse<Void>> clearCart() {
        cartService.clearCart();
        return ResponseEntity.ok(ApiResponse.success("Cart cleared", null));
    }
}