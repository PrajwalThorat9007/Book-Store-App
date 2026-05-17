package com.bookstore.modules.customer.dto;

/*
 * This is the response DTO for a customer delivery address.
 * Returned after add, update, or fetch address operations.
 * Maps directly from the Address entity — no sensitive data included.
 */

import lombok.Data;

@Data
public class AddressResponse {

    // Unique database ID of the address record
    private Long id;

    // Primary address line
    private String line1;

    // Secondary address line — may be null if not provided
    private String line2;

    // City name
    private String city;

    // State name
    private String state;

    // 6-digit postal pincode
    private String pincode;

    // True if this is the customer's default delivery address
    private Boolean isDefault;
}
