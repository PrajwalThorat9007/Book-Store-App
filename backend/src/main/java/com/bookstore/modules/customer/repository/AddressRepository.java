package com.bookstore.modules.customer.repository;

import com.bookstore.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Address entity.
 *
 * This repository handles all database operations related to
 * customer delivery addresses.
 *
 * JpaRepository already provides common CRUD methods like:
 * - save()
 * - findById()
 * - findAll()
 * - delete()
 *
 * Additional custom query methods can be created
 * using Spring Data JPA naming conventions.
 */
@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    /**
     * Returns all addresses belonging to a specific customer profile.
     *
     * Relationship path:
     * Address -> CustomerProfile -> id
     *
     * This is commonly used when displaying
     * all saved delivery addresses of a customer.
     *
     * @param profileId customer profile id
     * @return list of addresses
     */
    List<Address> findByCustomerProfileId(Long profileId);

    /**
     * Finds the default address of a customer profile.
     *
     * Only one address should ideally have:
     * isDefault = true
     *
     * This is useful during checkout or delivery selection.
     *
     * Relationship path:
     * Address -> CustomerProfile -> id
     *
     * @param profileId customer profile id
     * @return optional default address
     */
    Optional<Address> findByCustomerProfileIdAndIsDefaultTrue(Long profileId);
}