package com.bookstore.modules.wishlist.service;

/*
 * This is the service layer class for the Wishlist module.
 * It contains all business logic for managing a user's saved product wishlist.
 * Key rules enforced here:
 *   - A wishlist is auto-created if the user doesn't have one yet
 *   - Duplicate products in the same wishlist are rejected
 *   - The logged-in user is read from Spring SecurityContext — no userId parameter needed
 */

import com.bookstore.entity.Product;
import com.bookstore.entity.User;
import com.bookstore.entity.Wishlist;
import com.bookstore.entity.WishlistItem;
import com.bookstore.modules.product.repository.ProductRepository;
import com.bookstore.modules.user.repository.UserRepository;
import com.bookstore.modules.wishlist.dto.WishlistItemResponse;
import com.bookstore.modules.wishlist.dto.WishlistResponse;
import com.bookstore.modules.wishlist.repository.WishlistItemRepository;
import com.bookstore.modules.wishlist.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final WishlistItemRepository wishlistItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    // Returns the current user's wishlist — auto-creates an empty wishlist if none exists
    @Transactional(readOnly = true)
    public WishlistResponse getWishlist() {
        User user = getCurrentUser();
        Wishlist wishlist = wishlistRepository.findByUserId(user.getId())
                .orElseGet(() -> createEmptyWishlist(user));
        return mapToResponse(wishlist);
    }

    // Adds a product to the wishlist — rejects if the product is already saved
    public WishlistResponse addToWishlist(Long productId) {
        User user = getCurrentUser();
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found: " + productId));

        Wishlist wishlist = wishlistRepository.findByUserId(user.getId())
                .orElseGet(() -> createEmptyWishlist(user));

        // Prevent duplicate entries — each product can only appear once in a wishlist
        if (wishlistItemRepository.existsByWishlistIdAndProductId(wishlist.getId(), productId)) {
            throw new RuntimeException("Product is already in your wishlist");
        }

        WishlistItem item = new WishlistItem();
        item.setWishlist(wishlist);
        item.setProduct(product);
        wishlistItemRepository.save(item);

        log.info("Product {} added to wishlist for user {}", productId, user.getEmail());
        return mapToResponse(wishlist);
    }

    // Removes a product from the wishlist — throws if the product is not in the wishlist
    public void removeFromWishlist(Long productId) {
        User user = getCurrentUser();
        Wishlist wishlist = wishlistRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Wishlist not found"));

        WishlistItem item = wishlistItemRepository
                .findByWishlistIdAndProductId(wishlist.getId(), productId)
                .orElseThrow(() -> new RuntimeException("Product not found in wishlist"));

        wishlistItemRepository.delete(item);
        log.info("Product {} removed from wishlist for user {}", productId, user.getEmail());
    }

    // Checks if a specific product is already in the current user's wishlist — returns false if no wishlist exists
    @Transactional(readOnly = true)
    public boolean isInWishlist(Long productId) {
        User user = getCurrentUser();
        return wishlistRepository.findByUserId(user.getId())
                .map(w -> wishlistItemRepository.existsByWishlistIdAndProductId(w.getId(), productId))
                .orElse(false);
    }

    // Reads the authenticated user's email from SecurityContext and fetches the User entity
    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));
    }

    // Creates and saves a new empty wishlist for a user who doesn't have one yet
    private Wishlist createEmptyWishlist(User user) {
        Wishlist wishlist = new Wishlist();
        wishlist.setUser(user);
        return wishlistRepository.save(wishlist);
    }

    // Converts a Wishlist entity to a WishlistResponse DTO including all saved items
    private WishlistResponse mapToResponse(Wishlist wishlist) {
        List<WishlistItemResponse> items = wishlistItemRepository
                .findByWishlistId(wishlist.getId())
                .stream()
                .map(this::mapItemToResponse)
                .collect(Collectors.toList());

        return WishlistResponse.builder()
                .id(wishlist.getId())
                .userId(wishlist.getUser().getId())
                .items(items)
                .totalItems(items.size())
                .build();
    }

    // Converts a WishlistItem entity to a WishlistItemResponse DTO — includes stock availability flag
    private WishlistItemResponse mapItemToResponse(WishlistItem item) {
        Product p = item.getProduct();
        return WishlistItemResponse.builder()
                .id(item.getId())
                .productId(p.getId())
                .productTitle(p.getTitle())
                .productAuthor(p.getAuthor())
                .productImageUrl(p.getImageUrl())
                .productPrice(p.getPrice())
                // inStock is true if at least 1 unit is available
                .inStock(p.getStockQuantity() > 0)
                .build();
    }
}
