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
@RestController
@RequestMapping("/api/customers")
public class CustomerController {
    // TODO: Implement customer controller endpoints
}
