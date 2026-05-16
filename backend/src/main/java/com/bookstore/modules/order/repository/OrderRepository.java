// Manas: Module changed → modules/order/repository
// What's changed: New file. JPA Repository for Order entity.
//                 Custom queries for fetching by user and by id+user combo.

package com.bookstore.modules.order.repository;

import com.bookstore.entity.Order;
import com.bookstore.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Order entity.
 *
 * Manas: findByUserOrderByCreatedAtDesc → gets a user's orders newest-first.
 *        findByIdAndUser → fetches an order only if it belongs to that user.
 *        This prevents one user from seeing another user's order details.
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    // Manas: Returns all orders for a user, sorted newest first
    List<Order> findByUserOrderByCreatedAtDesc(User user);

    // Manas: Ownership check built into the query — returns empty if order
    //        exists but belongs to a different user
    Optional<Order> findByIdAndUser(Long id, User user);
}