// Manas: Module changed → modules/order/dto
// What's changed: New file. DTO for a single item inside an order response.
//                 Includes subtotal (calculated, not stored in DB).

package com.bookstore.modules.order.dto;

import lombok.Data;
import java.math.BigDecimal;

/**
 * Response DTO for one item inside an Order.
 *
 * Manas: subtotal = unitPrice × quantity
 *        Calculated in OrderService.mapToOrderResponse()
 *        We never store subtotal in DB — always compute on the fly.
 */
@Data
public class OrderItemResponse {

    private Long id;
    private Long productId;
    private String productTitle;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal subtotal;  // unitPrice × quantity
}