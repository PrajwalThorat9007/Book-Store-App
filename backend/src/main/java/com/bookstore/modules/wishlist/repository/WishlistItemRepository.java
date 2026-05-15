package com.bookstore.modules.wishlist.repository;

import com.bookstore.entity.WishlistItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for WishlistItem entity
 * 
 * TODO: Implement the following methods:
 * - boolean existsByWishlistIdAndProductId(Long wishlistId, Long productId)
 * - Optional<WishlistItem> findByWishlistIdAndProductId(Long wishlistId, Long productId)
 * - List<WishlistItem> findByWishlistId(Long wishlistId)
 */
@Repository
public interface WishlistItemRepository extends JpaRepository<WishlistItem, Long> {
    // TODO: Add custom query methods
}
