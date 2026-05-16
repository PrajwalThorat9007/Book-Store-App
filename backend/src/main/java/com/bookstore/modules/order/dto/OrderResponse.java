// Manas: Module changed → modules/order/dto
// What's changed: New file. Full order response DTO returned to the client.
//                 Contains flattened user info, address string, and list of items.

package com.bookstore.modules.order.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Response DTO for Order endpoints (GET and POST responses).
 *
 * Manas: We flatten the delivery address into one readable string here.
 *        We never return the full Address entity — only what the client needs.
 *        Items is a List<OrderItemResponse> — each item mapped separately.
 */
@Data
public class OrderResponse {

    private Long id;
    private Long userId;
    private String userName;
    private String deliveryAddress;   // "line1, city, state pincode"
    private BigDecimal totalAmount;
    private String status;            // PENDING / CONFIRMED / SHIPPED / DELIVERED / CANCELLED
    private LocalDateTime createdAt;
    private List<OrderItemResponse> items;
}