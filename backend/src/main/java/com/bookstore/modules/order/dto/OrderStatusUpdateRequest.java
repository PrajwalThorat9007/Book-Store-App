package com.bookstore.modules.order.dto;

/*
 * This is the request DTO for updating an order's status.
 * Used by the admin endpoint PUT /api/orders/{id}/status.
 * The @Pattern annotation restricts the value to only valid status strings.
 */

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class OrderStatusUpdateRequest {

    // New status value — must be one of the five allowed states
    @NotBlank(message = "Status is required")
    @Pattern(
            regexp = "PENDING|CONFIRMED|SHIPPED|DELIVERED|CANCELLED",
            message = "Status must be one of: PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED"
    )
    private String status;
}
