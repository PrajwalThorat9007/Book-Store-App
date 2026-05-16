// Manas: Module changed → modules/order/repository
// What's changed: New file. JPA Repository for OrderItem entity.
//                 One custom query to get all items belonging to an order.

package com.bookstore.modules.order.repository;

import com.bookstore.entity.Order;
import com.bookstore.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for OrderItem entity.
 *
 * Manas: findByOrder → fetches all line items for a given order.
 *        Used in OrderService when building the OrderResponse.
 */
@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    // Manas: Gets all items that belong to a specific order
    List<OrderItem> findByOrder(Order order);
}