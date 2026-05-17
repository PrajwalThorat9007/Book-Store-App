package com.bookstore.modules.order.dto;

/*
 * This is the request DTO for placing a new order.
 * The client only needs to send the delivery address ID.
 * Cart items and prices are always read server-side — never trusted from the frontend.
 */

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderRequest {

    // ID of the delivery address the user wants the order shipped to
    @NotNull(message = "Delivery address ID is required")
    private Long deliveryAddressId;
}
