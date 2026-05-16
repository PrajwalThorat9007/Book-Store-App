package com.bookstore.modules.customer.dto;

/**
 * DTO for Customer Details Request
 * 
 * TODO: Implement the following fields:
 * - String phone (@Pattern for phone validation)
 * - String preferenceNotes
 * - AddressRequest defaultAddress
 * 
 * TODO: Add validation annotations
 */

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * DTO used for creating or updating
 * customer profile details.
 */
@Data
public class CustomerDetailsRequest {

    /**
     * Customer phone number.
     *
     * Validation:
     * Must contain exactly 10 digits.
     */
    @Pattern(
            regexp = "^[0-9]{10}$",
            message = "Phone number must contain exactly 10 digits"
    )
    private String phone;

    /**
     * Additional customer preferences or notes.
     *
     * Example:
     * "Deliver only during evening"
     */
    private String preferenceNotes;

    /**
     * Default delivery address.
     *
     * @Valid ensures nested DTO validation.
     */
    @Valid
    private AddressRequest defaultAddress;
}
