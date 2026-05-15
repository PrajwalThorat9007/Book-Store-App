package com.bookstore.modules.order.repository;

import com.bookstore.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for Order entity
 * 
 * TODO: Implement the following methods:
 * - List<Order> findByUserIdOrderByCreatedAtDesc(Long userId)
 * - Optional<Order> findByIdAndUserId(Long orderId, Long userId)
 * - List<Order> findAllByOrderByCreatedAtDesc()
 * - List<Order> findByStatus(OrderStatus status)
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    // TODO: Add custom query methods
}
