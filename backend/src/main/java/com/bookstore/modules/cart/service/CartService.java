package com.bookstore.modules.cart.service;

/*
 * This is the service layer class for the Cart module.
 * It contains all business logic for managing a user's shopping cart.
 * Handles stock validation, quantity merging, total recalculation, and cart auto-creation.
 * Reads the logged-in user from Spring SecurityContext — no userId parameter needed in methods.
 */

import com.bookstore.entity.Cart;
import com.bookstore.entity.CartItem;
import com.bookstore.entity.Product;
import com.bookstore.entity.User;
import com.bookstore.modules.cart.dto.*;
import com.bookstore.modules.cart.repository.CartItemRepository;
import com.bookstore.modules.cart.repository.CartRepository;
import com.bookstore.modules.user.repository.UserRepository;
import com.bookstore.modules.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    // Returns the current user's cart — auto-creates an empty cart if none exists
    @Transactional(readOnly = true)
    public CartResponse getCart() {
        User user = getCurrentUser();
        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseGet(() -> createEmptyCart(user));
        return mapToCartResponse(cart);
    }

    // Adds a product to the cart — merges quantity if already present, validates stock before adding
    public CartResponse addToCart(AddToCartRequest request) {
        User user = getCurrentUser();
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found: " + request.getProductId()));

        // Reject if requested quantity exceeds available stock
        if (product.getStockQuantity() < request.getQuantity()) {
            throw new RuntimeException("Insufficient stock. Available: " + product.getStockQuantity());
        }

        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseGet(() -> createEmptyCart(user));

        // If product already in cart, add to existing quantity; otherwise create a new cart item
        cartItemRepository.findByCartIdAndProductId(cart.getId(), product.getId())
                .ifPresentOrElse(existingItem -> {
                    int newQty = existingItem.getQuantity() + request.getQuantity();
                    if (product.getStockQuantity() < newQty) {
                        throw new RuntimeException("Insufficient stock. Available: " + product.getStockQuantity());
                    }
                    existingItem.setQuantity(newQty);
                    cartItemRepository.save(existingItem);
                }, () -> {
                    CartItem newItem = new CartItem();
                    newItem.setCart(cart);
                    newItem.setProduct(product);
                    newItem.setQuantity(request.getQuantity());
                    newItem.setUnitPrice(product.getPrice());
                    cart.getCartItems().add(newItem);
                });

        recalculateTotal(cart);
        cartRepository.save(cart);
        log.info("Item added to cart - user: {}, productId: {}", user.getEmail(), product.getId());
        return mapToCartResponse(cart);
    }

    // Updates the quantity of a specific product in the cart — validates stock before updating
    public CartResponse updateQuantity(Long productId, UpdateCartItemRequest request) {
        User user = getCurrentUser();
        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Cart not found for user"));
        CartItem item = cartItemRepository.findByCartIdAndProductId(cart.getId(), productId)
                .orElseThrow(() -> new RuntimeException("Product not found in cart"));

        // Reject if new quantity exceeds available stock
        if (item.getProduct().getStockQuantity() < request.getQuantity()) {
            throw new RuntimeException("Insufficient stock. Available: " + item.getProduct().getStockQuantity());
        }

        item.setQuantity(request.getQuantity());
        cartItemRepository.save(item);
        recalculateTotal(cart);
        cartRepository.save(cart);
        return mapToCartResponse(cart);
    }

    // Removes a specific product from the cart and recalculates the total
    public CartResponse removeItem(Long productId) {
        User user = getCurrentUser();
        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Cart not found for user"));
        CartItem item = cartItemRepository.findByCartIdAndProductId(cart.getId(), productId)
                .orElseThrow(() -> new RuntimeException("Product not found in cart"));

        cart.getCartItems().removeIf(i -> i.getId().equals(item.getId()));
        recalculateTotal(cart);
        cartRepository.save(cart);
        return mapToCartResponse(cart);
    }

    // Removes all items from the cart and resets the total amount to zero
    public void clearCart() {
        User user = getCurrentUser();
        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Cart not found for user"));
        cart.getCartItems().clear();
        cart.setTotalAmount(BigDecimal.ZERO);
        cartRepository.save(cart);
        log.info("Cart cleared - user: {}", user.getEmail());
    }

    // Reads the authenticated user's email from SecurityContext and fetches the User entity
    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));
    }

    // Creates and saves a new empty cart for a user who doesn't have one yet
    private Cart createEmptyCart(User user) {
        Cart cart = new Cart();
        cart.setUser(user);
        cart.setTotalAmount(BigDecimal.ZERO);
        return cartRepository.save(cart);
    }

    // Recalculates the cart total by summing unitPrice × quantity for all items
    private void recalculateTotal(Cart cart) {
        BigDecimal total = cart.getCartItems().stream()
                .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        cart.setTotalAmount(total);
    }

    // Converts a Cart entity to a CartResponse DTO including all cart items
    private CartResponse mapToCartResponse(Cart cart) {
        CartResponse response = new CartResponse();
        response.setCartId(cart.getId());
        response.setUserId(cart.getUser().getId());
        List<CartItemResponse> items = cart.getCartItems().stream()
                .map(this::mapToCartItemResponse)
                .collect(Collectors.toList());
        response.setItems(items);
        response.setTotalAmount(cart.getTotalAmount());
        response.setTotalItems(items.size());
        return response;
    }

    // Converts a CartItem entity to a CartItemResponse DTO — calculates subtotal on the fly
    private CartItemResponse mapToCartItemResponse(CartItem item) {
        CartItemResponse r = new CartItemResponse();
        r.setCartItemId(item.getId());
        r.setProductId(item.getProduct().getId());
        r.setTitle(item.getProduct().getTitle());
        r.setAuthor(item.getProduct().getAuthor());
        r.setImageUrl(item.getProduct().getImageUrl());
        r.setQuantity(item.getQuantity());
        r.setUnitPrice(item.getUnitPrice());
        r.setSubtotal(item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        return r;
    }
}
