package com.bookstore.modules.order.repository;

import com.bookstore.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for OrderItem entity
 * 
 * TODO: Implement the following methods:
 * - List<OrderItem> findByOrderId(Long orderId)
 */
@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    // TODO: Add custom query methods
}
