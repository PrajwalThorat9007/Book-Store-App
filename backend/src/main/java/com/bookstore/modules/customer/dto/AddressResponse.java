package com.bookstore.modules.customer.dto;

/**
 * DTO for Address Response
 * 
 * TODO: Implement the following fields:
 * - Long id
 * - String line1
 * - String line2
 * - String city
 * - String state
 * - String pincode
 * - Boolean isDefault
 */

import lombok.Data;

/**
 * DTO returned to the client
 * after address operations.
 *
 * This class is used in API responses.
 */
@Data
public class AddressResponse {

    /**
     * Unique address id.
     */
    private Long id;

    /**
     * Primary address line.
     */
    private String line1;

    /**
     * Secondary address line.
     */
    private String line2;

    /**
     * City name.
     */
    private String city;

    /**
     * State name.
     */
    private String state;

    /**
     * Postal pincode.
     */
    private String pincode;

    /**
     * Indicates whether this is
     * the default delivery address.
     */
    private Boolean isDefault;
}