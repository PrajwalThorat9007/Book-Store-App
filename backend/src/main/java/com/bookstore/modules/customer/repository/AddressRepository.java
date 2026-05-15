package com.bookstore.modules.customer.repository;

import com.bookstore.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for Address entity
 * 
 * TODO: Implement the following methods:
 * - List<Address> findByCustomerProfileId(Long profileId)
 * - Optional<Address> findByCustomerProfileIdAndIsDefaultTrue(Long profileId)
 */
@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    // TODO: Add custom query methods
}
