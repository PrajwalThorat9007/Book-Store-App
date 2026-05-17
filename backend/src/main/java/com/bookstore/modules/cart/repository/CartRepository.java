package com.bookstore.modules.cart.repository;

/*
 * This is the repository interface for the Cart module.
 * It extends JpaRepository to get standard CRUD operations for free.
 * Provides custom methods to look up a cart by user ID or User entity.
 */

import com.bookstore.entity.Cart;
import com.bookstore.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    // Finds a cart by the user's ID — used in CartService to load the current user's cart
    Optional<Cart> findByUserId(Long userId);

    // Finds a cart by the User entity directly — used in OrderService when placing an order
    Optional<Cart> findByUser(User user);
}
