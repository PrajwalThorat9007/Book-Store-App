package com.bookstore.modules.order.repository;

/*
 * This is the repository interface for the Order module.
 * It extends JpaRepository to get standard CRUD operations for free.
 * Provides custom queries to fetch orders by user and to enforce ownership checks.
 */

import com.bookstore.entity.Order;
import com.bookstore.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    // Returns all orders for a user sorted newest first — used in order history page
    List<Order> findByUserOrderByCreatedAtDesc(User user);

    // Fetches an order only if it belongs to the given user — prevents users from viewing others' orders
    Optional<Order> findByIdAndUser(Long id, User user);
}
