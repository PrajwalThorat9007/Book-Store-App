package com.bookstore.modules.customer.dto;

/**
 * DTO for Customer Details Response
 * 
 * TODO: Implement the following fields:
 * - Long id
 * - Long userId
 * - String phone
 * - String preferenceNotes
 * - List<AddressResponse> addresses
 * - AddressResponse defaultAddress
 */

import lombok.Data;

import java.util.List;

/**
 * DTO returned to the client
 * when customer profile details are requested.
 */
@Data
public class CustomerDetailsResponse {

    /**
     * Customer profile id.
     */
    private Long id;

    /**
     * Associated user id.
     */
    private Long userId;

    /**
     * Customer phone number.
     */
    private String phone;

    /**
     * Customer preference notes.
     */
    private String preferenceNotes;

    /**
     * List of all saved customer addresses.
     */
    private List<AddressResponse> addresses;

    /**
     * Customer default address.
     */
    private AddressResponse defaultAddress;
}
