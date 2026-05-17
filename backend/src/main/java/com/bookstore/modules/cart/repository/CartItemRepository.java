package com.bookstore.modules.cart.repository;

/*
 * This is the repository interface for CartItem entities.
 * It extends JpaRepository to get standard CRUD operations for free.
 * Provides custom methods to find items by cart+product combination and by cart entity.
 */

import com.bookstore.entity.Cart;
import com.bookstore.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    // Finds a specific item in a cart by product ID — used to check if product is already in cart
    Optional<CartItem> findByCartIdAndProductId(Long cartId, Long productId);

    // Returns all items belonging to a cart — used in OrderService to read cart contents when placing an order
    List<CartItem> findByCart(Cart cart);
}
