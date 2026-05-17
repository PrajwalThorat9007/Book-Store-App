package com.bookstore.modules.customer.repository;

/*
 * This is the repository interface for the Address entity.
 * It extends JpaRepository to get standard CRUD operations for free.
 * Provides custom methods to fetch all addresses for a profile and find the default address.
 */

import com.bookstore.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    // Returns all addresses belonging to a customer profile — used when displaying saved addresses
    List<Address> findByCustomerProfileId(Long profileId);

    // Finds the address marked as default for a customer profile — used during checkout
    Optional<Address> findByCustomerProfileIdAndIsDefaultTrue(Long profileId);
}
