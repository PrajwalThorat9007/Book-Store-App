// Manas: Module changed → modules/order/dto
// What's changed: New file. DTO for placing a new order.
//                 User sends only the delivery address ID — everything
//                 else (cart items, price) is read server-side.

package com.bookstore.modules.order.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Request DTO for POST /api/orders (place order)
 *
 * Manas: The user only needs to tell us WHICH address to deliver to.
 *        Cart items and prices are always read from the DB — never trusted from frontend.
 */
@Data
public class OrderRequest {

    @NotNull(message = "Delivery address ID is required")
    private Long deliveryAddressId;
}