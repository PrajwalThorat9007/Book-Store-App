package com.bookstore.modules.customer.controller;

import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for Customer Profile operations
 * 
 * TODO: Implement the following endpoints:
 * - GET    /api/customers/details        - Get customer profile
 * - PUT    /api/customers/details        - Update customer profile
 * - POST   /api/customers/addresses      - Add new address
 * - PUT    /api/customers/addresses/{id} - Update address
 * - DELETE /api/customers/addresses/{id} - Delete address
 * - PUT    /api/customers/addresses/{id}/default - Set as default address
 * 
 * TODO: Add security:
 * - @PreAuthorize("hasRole('USER')") on all methods
 * - Get userId from SecurityContext
 * 
 * TODO: Add validation:
 * - @Valid on request bodies
 * 
 * TODO: Return proper HTTP status codes
 */

import com.bookstore.modules.customer.dto.AddressRequest;
import com.bookstore.modules.customer.dto.AddressResponse;
import com.bookstore.modules.customer.dto.CustomerDetailsRequest;
import com.bookstore.modules.customer.dto.CustomerDetailsResponse;
import com.bookstore.modules.customer.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for Customer Profile operations.
 *
 * This controller handles customer-related APIs such as:
 * - Customer profile management
 * - Address management
 * - Default address selection
 *
 * Base URL:
 * /api/customers
 */
@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    /**
     * Service layer dependency.
     *
     * Controller delegates business logic to service layer.
     */
    private final CustomerService customerService;

    /**
     * Constructor Injection.
     */
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    /**
     * Fetches customer profile details.
     *
     * Endpoint:
     * GET /api/customers/details
     *
     * @return customer details response
     */
    @GetMapping("/details")
    public ResponseEntity<CustomerDetailsResponse> getCustomerDetails() {

        // TODO:
        // Replace hardcoded userId with SecurityContext user later
        Long userId = 1L;

        CustomerDetailsResponse response =
                customerService.getCustomerDetails(userId);

        return ResponseEntity.ok(response);
    }

    /**
     * Updates customer profile details.
     *
     * Endpoint:
     * PUT /api/customers/details
     *
     * @param request customer details request body
     * @return updated customer details
     */
    @PutMapping("/details")
    public ResponseEntity<CustomerDetailsResponse> updateCustomerDetails(
            @Valid @RequestBody CustomerDetailsRequest request
    ) {

        Long userId = 1L;

        CustomerDetailsResponse response =
                customerService.updateCustomerDetails(userId, request);

        return ResponseEntity.ok(response);
    }

    /**
     * Adds a new customer address.
     *
     * Endpoint:
     * POST /api/customers/addresses
     *
     * @param request address request body
     * @return saved address response
     */
    @PostMapping("/addresses")
    public ResponseEntity<AddressResponse> addAddress(
            @Valid @RequestBody AddressRequest request
    ) {

        Long userId = 1L;

        AddressResponse response =
                customerService.addAddress(userId, request);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Updates an existing customer address.
     *
     * Endpoint:
     * PUT /api/customers/addresses/{id}
     *
     * @param id address id
     * @param request updated address request
     * @return updated address response
     */
    @PutMapping("/addresses/{id}")
    public ResponseEntity<AddressResponse> updateAddress(
            @PathVariable Long id,
            @Valid @RequestBody AddressRequest request
    ) {

        Long userId = 1L;

        AddressResponse response =
                customerService.updateAddress(userId, id, request);

        return ResponseEntity.ok(response);
    }

    /**
     * Deletes a customer address.
     *
     * Endpoint:
     * DELETE /api/customers/addresses/{id}
     *
     * @param id address id
     * @return success response
     */
    @DeleteMapping("/addresses/{id}")
    public ResponseEntity<Void> deleteAddress(
            @PathVariable Long id
    ) {

        Long userId = 1L;

        customerService.deleteAddress(userId, id);

        return ResponseEntity.noContent().build();
    }

    /**
     * Sets an address as default.
     *
     * Endpoint:
     * PUT /api/customers/addresses/{id}/default
     *
     * @param id address id
     * @return success response
     */
    @PutMapping("/addresses/{id}/default")
    public ResponseEntity<Void> setDefaultAddress(
            @PathVariable Long id
    ) {

        Long userId = 1L;

        customerService.setDefaultAddress(userId, id);

        return ResponseEntity.ok().build();
    }
}
