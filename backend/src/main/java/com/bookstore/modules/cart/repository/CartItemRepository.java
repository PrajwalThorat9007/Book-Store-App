package com.bookstore.modules.cart.repository;

import com.bookstore.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for CartItem entity
 * 
 * TODO: Implement the following methods:
 * - Optional<CartItem> findByCartIdAndProductId(Long cartId, Long productId)
 * - void deleteByCartId(Long cartId)
 * - List<CartItem> findByCartId(Long cartId)
 */
@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    // TODO: Add custom query methods
}
