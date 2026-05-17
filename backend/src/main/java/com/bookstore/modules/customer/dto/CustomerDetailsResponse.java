package com.bookstore.modules.customer.dto;

/*
 * This is the response DTO for customer profile details.
 * Returned by GET and PUT /api/customers/details.
 * Includes the full list of saved addresses and highlights the default one separately.
 */

import lombok.Data;

import java.util.List;

@Data
public class CustomerDetailsResponse {

    // Unique ID of the customer profile record
    private Long id;

    // ID of the associated user account
    private Long userId;

    // Customer's phone number — may be null if not yet provided
    private String phone;

    // Customer's delivery preference notes — may be null if not yet provided
    private String preferenceNotes;

    // All saved delivery addresses for this customer
    private List<AddressResponse> addresses;

    // The address currently marked as default — null if no default is set
    private AddressResponse defaultAddress;
}
