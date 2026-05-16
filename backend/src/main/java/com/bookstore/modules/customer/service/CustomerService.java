package com.bookstore.modules.customer.service;

import org.springframework.stereotype.Service;

/**
 * Service for Customer Profile operations
 * 
 * TODO: Implement the following methods:
 * - CustomerDetailsResponse getCustomerDetails(Long userId)
 * - CustomerDetailsResponse updateCustomerDetails(Long userId, CustomerDetailsRequest request)
 * - AddressResponse addAddress(Long userId, AddressRequest request)
 * - AddressResponse updateAddress(Long userId, Long addressId, AddressRequest request)
 * - void deleteAddress(Long userId, Long addressId)
 * - void setDefaultAddress(Long userId, Long addressId)
 * 
 * TODO: Inject dependencies:
 * - CustomerProfileRepository
 * - AddressRepository
 * - UserRepository (to validate user exists)
 * 
 * TODO: Handle business logic:
 * - Create profile if doesn't exist
 * - Ensure only one default address per profile
 * - Validate address belongs to user before update/delete
 */


import com.bookstore.modules.customer.dto.AddressRequest;
import com.bookstore.modules.customer.dto.AddressResponse;
import com.bookstore.modules.customer.dto.CustomerDetailsRequest;
import com.bookstore.modules.customer.dto.CustomerDetailsResponse;
import com.bookstore.modules.customer.repository.AddressRepository;
import com.bookstore.modules.customer.repository.CustomerProfileRepository;
import com.bookstore.modules.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service layer for Customer Profile operations.
 *
 * This class contains the business logic of the customer module.
 *
 * Responsibilities:
 * - Manage customer profile details
 * - Manage customer delivery addresses
 * - Ensure only one default address exists
 * - Validate ownership before updating/deleting addresses
 *
 * Architecture Flow:
 * Controller -> Service -> Repository -> Database
 */
@Service
public class CustomerService {

    /**
     * Repository for customer profile operations.
     */
    private final CustomerProfileRepository customerProfileRepository;

    /**
     * Repository for address operations.
     */
    private final AddressRepository addressRepository;

    /**
     * Repository used to validate whether a user exists.
     */
    private final UserRepository userRepository;

    /**
     * Constructor Injection.
     *
     * Spring automatically injects dependencies here.
     */
    public CustomerService(
            CustomerProfileRepository customerProfileRepository,
            AddressRepository addressRepository,
            UserRepository userRepository
    ) {
        this.customerProfileRepository = customerProfileRepository;
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
    }

    /**
     * Fetches customer profile details.
     *
     * TODO:
     * - Find customer profile using user id
     * - Convert entity into response DTO
     *
     * @param userId logged-in user id
     * @return customer details response
     */
    public CustomerDetailsResponse getCustomerDetails(Long userId) {

        // TODO: Implement logic

        return null;
    }

    /**
     * Creates or updates customer profile details.
     *
     * Business Logic:
     * - Validate user exists
     * - Create profile if not available
     * - Update phone and preference notes
     *
     * @param userId logged-in user id
     * @param request customer details request DTO
     * @return updated customer details response
     */
    public CustomerDetailsResponse updateCustomerDetails(
            Long userId,
            CustomerDetailsRequest request
    ) {

        // TODO: Implement logic

        return null;
    }

    /**
     * Adds a new customer address.
     *
     * Business Rules:
     * - Address must belong to logged-in user
     * - First address becomes default automatically
     * - Only one default address should exist
     *
     * @param userId logged-in user id
     * @param request address request DTO
     * @return saved address response
     */
    public AddressResponse addAddress(
            Long userId,
            AddressRequest request
    ) {

        // TODO: Implement logic

        return null;
    }

    /**
     * Updates an existing address.
     *
     * Business Rules:
     * - User can update only their own addresses
     * - If address becomes default,
     *   other addresses must become non-default
     *
     * @param userId logged-in user id
     * @param addressId address id
     * @param request updated address request
     * @return updated address response
     */
    public AddressResponse updateAddress(
            Long userId,
            Long addressId,
            AddressRequest request
    ) {

        // TODO: Implement logic

        return null;
    }

    /**
     * Deletes an existing customer address.
     *
     * Business Rules:
     * - User can delete only their own addresses
     *
     * @param userId logged-in user id
     * @param addressId address id
     */
    public void deleteAddress(
            Long userId,
            Long addressId
    ) {

        // TODO: Implement logic
    }

    /**
     * Sets an address as default.
     *
     * Business Logic:
     * - Remove default flag from all other addresses
     * - Set selected address as default
     *
     * @param userId logged-in user id
     * @param addressId address id
     */
    public void setDefaultAddress(
            Long userId,
            Long addressId
    ) {

        // TODO: Implement logic
    }

    /**
     * Returns all addresses of a customer.
     *
     * @param userId logged-in user id
     * @return list of customer addresses
     */
    public List<AddressResponse> getAllAddresses(Long userId) {

        // TODO: Implement logic

        return null;
    }
}