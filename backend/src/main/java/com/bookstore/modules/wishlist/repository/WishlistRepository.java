package com.bookstore.modules.wishlist.repository;

/*
 * This is the repository interface for the Wishlist entity.
 * It extends JpaRepository to get standard CRUD operations for free.
 * Provides a custom method to look up a wishlist by the associated user ID.
 */

import com.bookstore.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Long> {

    // Finds the wishlist belonging to a specific user — each user has at most one wishlist
    Optional<Wishlist> findByUserId(Long userId);
}
