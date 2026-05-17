package com.bookstore.modules.customer.dto;

/*
 * This is the request DTO for updating customer profile details.
 * Used by PUT /api/customers/details.
 * All fields are optional — only provided fields will be updated in the service.
 */

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class CustomerDetailsRequest {

    // Customer phone number — optional, must be exactly 10 digits if provided
    @Pattern(
            regexp = "^[0-9]{10}$",
            message = "Phone number must contain exactly 10 digits"
    )
    private String phone;

    // Free-text delivery preferences — e.g. "Deliver only during evening hours"
    private String preferenceNotes;

    // Optional default address to save along with the profile update
    // @Valid triggers nested validation on the AddressRequest fields
    @Valid
    private AddressRequest defaultAddress;
}
