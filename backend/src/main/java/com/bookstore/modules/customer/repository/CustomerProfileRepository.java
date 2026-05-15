package com.bookstore.modules.customer.repository;

import com.bookstore.entity.CustomerProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for CustomerProfile entity
 * 
 * TODO: Implement the following methods:
 * - Optional<CustomerProfile> findByUserId(Long userId)
 */
@Repository
public interface CustomerProfileRepository extends JpaRepository<CustomerProfile, Long> {
    // TODO: Add custom query methods
}
