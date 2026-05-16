// Manas: Module changed → modules/order/dto
// What's changed: New file. Admin sends this to update an order's status.
//                 @Pattern restricts to only valid status values.

package com.bookstore.modules.order.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * Request DTO for PUT /api/orders/{id}/status (admin update status)
 *
 * Manas: @Pattern validation ensures only valid status strings are accepted.
 *        Any other value → 400 Bad Request (handled by GlobalExceptionHandler).
 */
@Data
public class OrderStatusUpdateRequest {

    @NotBlank(message = "Status is required")
    @Pattern(
            regexp = "PENDING|CONFIRMED|SHIPPED|DELIVERED|CANCELLED",
            message = "Status must be one of: PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED"
    )
    private String status;
}