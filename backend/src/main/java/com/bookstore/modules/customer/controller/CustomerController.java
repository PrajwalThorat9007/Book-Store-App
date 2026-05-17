package com.bookstore.modules.customer.controller;

/*
 * This is the controller layer for the Customer module.
 * It handles all HTTP requests related to customer profile and delivery address management.
 * Currently uses a hardcoded userId = 1L — will be replaced with SecurityContext user once JWT is wired up.
 * Delegates all business logic to CustomerService.
 * Base URL: /api/customers
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

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    // Constructor injection — Spring automatically provides the CustomerService bean
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    // Returns the customer profile for the logged-in user — phone, preferences, and saved addresses
    @GetMapping("/details")
    public ResponseEntity<CustomerDetailsResponse> getCustomerDetails() {
        // TODO: Replace hardcoded userId with SecurityContext user once JWT is wired up
        Long userId = 1L;
        CustomerDetailsResponse response = customerService.getCustomerDetails(userId);
        return ResponseEntity.ok(response);
    }

    // Creates or updates the customer profile — phone number and preference notes
    @PutMapping("/details")
    public ResponseEntity<CustomerDetailsResponse> updateCustomerDetails(
            @Valid @RequestBody CustomerDetailsRequest request) {
        Long userId = 1L;
        CustomerDetailsResponse response = customerService.updateCustomerDetails(userId, request);
        return ResponseEntity.ok(response);
    }

    // Adds a new delivery address — first address is automatically set as default
    @PostMapping("/addresses")
    public ResponseEntity<AddressResponse> addAddress(
            @Valid @RequestBody AddressRequest request) {
        Long userId = 1L;
        AddressResponse response = customerService.addAddress(userId, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Updates an existing address by ID — service validates the address belongs to this user
    @PutMapping("/addresses/{id}")
    public ResponseEntity<AddressResponse> updateAddress(
            @PathVariable Long id,
            @Valid @RequestBody AddressRequest request) {
        Long userId = 1L;
        AddressResponse response = customerService.updateAddress(userId, id, request);
        return ResponseEntity.ok(response);
    }

    // Deletes an address by ID — service validates the address belongs to this user
    @DeleteMapping("/addresses/{id}")
    public ResponseEntity<Void> deleteAddress(@PathVariable Long id) {
        Long userId = 1L;
        customerService.deleteAddress(userId, id);
        return ResponseEntity.noContent().build();
    }

    // Sets an address as the default delivery address — clears the default flag from all others
    @PutMapping("/addresses/{id}/default")
    public ResponseEntity<Void> setDefaultAddress(@PathVariable Long id) {
        Long userId = 1L;
        customerService.setDefaultAddress(userId, id);
        return ResponseEntity.ok().build();
    }
}
