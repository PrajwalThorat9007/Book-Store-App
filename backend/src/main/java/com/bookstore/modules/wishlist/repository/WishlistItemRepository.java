package com.bookstore.modules.wishlist.repository;

/*
 * This is the repository interface for WishlistItem entities.
 * It extends JpaRepository to get standard CRUD operations for free.
 * Provides custom methods to check for duplicates, find specific items, and list all items in a wishlist.
 */

import com.bookstore.entity.WishlistItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WishlistItemRepository extends JpaRepository<WishlistItem, Long> {

    // Checks if a product is already in a wishlist — used to prevent duplicate entries
    boolean existsByWishlistIdAndProductId(Long wishlistId, Long productId);

    // Finds a specific wishlist item by wishlist and product — used when removing an item
    Optional<WishlistItem> findByWishlistIdAndProductId(Long wishlistId, Long productId);

    // Returns all items in a wishlist — used when building the WishlistResponse DTO
    List<WishlistItem> findByWishlistId(Long wishlistId);
}
