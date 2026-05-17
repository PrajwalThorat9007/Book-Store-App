package com.bookstore.modules.order.dto;

/*
 * This is the response DTO for a single item inside an order.
 * It carries the product details and quantity for one line item.
 * Subtotal is calculated on the fly — it is not stored in the database.
 */

import lombok.Data;
import java.math.BigDecimal;

@Data
public class OrderItemResponse {

    // Unique ID of this order item record
    private Long id;

    // ID and title of the product ordered
    private Long productId;
    private String productTitle;

    // Number of units ordered
    private Integer quantity;

    // Price per unit at the time of ordering — snapshot, not live product price
    private BigDecimal unitPrice;

    // Calculated as unitPrice × quantity — computed in service, not stored in DB
    private BigDecimal subtotal;
}
