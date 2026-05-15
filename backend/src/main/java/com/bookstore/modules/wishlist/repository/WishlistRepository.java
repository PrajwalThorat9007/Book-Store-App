package com.bookstore.modules.wishlist.repository;

import com.bookstore.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for Wishlist entity
 * 
 * TODO: Implement the following methods:
 * - Optional<Wishlist> findByUserId(Long userId)
 */
@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    // TODO: Add custom query methods
}
