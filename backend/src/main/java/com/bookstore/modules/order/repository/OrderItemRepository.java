package com.bookstore.modules.order.repository;

/*
 * This is the repository interface for OrderItem entities.
 * It extends JpaRepository to get standard CRUD operations for free.
 * Provides a custom method to fetch all items belonging to a specific order.
 */

import com.bookstore.entity.Order;
import com.bookstore.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    // Returns all line items for a given order — used when building the OrderResponse
    List<OrderItem> findByOrder(Order order);
}
