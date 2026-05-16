package com.bookstore.modules.customer.dto;

/**
 * DTO for Address Request
 * 
 * TODO: Implement the following fields:
 * - String line1 (required, @NotBlank)
 * - String line2 (optional)
 * - String city (required, @NotBlank)
 * - String state (required, @NotBlank)
 * - String pincode (required, @Pattern for pincode validation)
 * - Boolean isDefault
 * 
 * TODO: Add validation annotations
 */

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * DTO used for creating or updating customer addresses.
 *
 * This class receives address data from the frontend/client.
 *
 * Validation annotations ensure proper input validation
 * before data reaches the service layer.
 */
@Data
public class AddressRequest {

    /**
     * Primary address line.
     *
     * Example:
     * Flat No 12, MG Road
     */
    @NotBlank(message = "Address line 1 is required")
    private String line1;

    /**
     * Secondary address line.
     *
     * Optional field.
     */
    private String line2;

    /**
     * City name.
     */
    @NotBlank(message = "City is required")
    private String city;

    /**
     * State name.
     */
    @NotBlank(message = "State is required")
    private String state;

    /**
     * Indian pincode validation.
     *
     * Must contain exactly 6 digits.
     */
    @NotBlank(message = "Pincode is required")
    @Pattern(
            regexp = "^[0-9]{6}$",
            message = "Pincode must contain exactly 6 digits"
    )
    private String pincode;

    /**
     * Indicates whether this address
     * should become the default address.
     */
    private Boolean isDefault;
}