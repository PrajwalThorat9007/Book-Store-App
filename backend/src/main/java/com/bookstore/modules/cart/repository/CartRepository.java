package com.bookstore.modules.cart.repository;

import com.bookstore.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for Cart entity
 * 
 * TODO: Implement the following methods:
 * - Optional<Cart> findByUserId(Long userId)
 * - void deleteByUserId(Long userId)
 */
@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    // TODO: Add custom query methods
}
