package com.bookstore.modules.customer.dto;

/*
 * This is the request DTO for creating or updating a customer delivery address.
 * Used by POST /api/customers/addresses and PUT /api/customers/addresses/{id}.
 * Validation annotations enforce required fields and a 6-digit pincode format.
 */

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class AddressRequest {

    // Primary address line — required, e.g. "Flat No 12, MG Road"
    @NotBlank(message = "Address line 1 is required")
    private String line1;

    // Secondary address line — optional, e.g. apartment number or landmark
    private String line2;

    // City name — required
    @NotBlank(message = "City is required")
    private String city;

    // State name — required
    @NotBlank(message = "State is required")
    private String state;

    // Indian pincode — required, must be exactly 6 digits
    @NotBlank(message = "Pincode is required")
    @Pattern(
            regexp = "^[0-9]{6}$",
            message = "Pincode must contain exactly 6 digits"
    )
    private String pincode;

    // If true, this address will be set as the default delivery address
    private Boolean isDefault;
}
