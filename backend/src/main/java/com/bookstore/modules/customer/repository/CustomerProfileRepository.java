package com.bookstore.modules.customer.repository;

/*
 * This is the repository interface for the CustomerProfile entity.
 * It extends JpaRepository to get standard CRUD operations for free.
 * Provides a custom method to look up a profile by the associated user ID.
 */

import com.bookstore.entity.CustomerProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerProfileRepository extends JpaRepository<CustomerProfile, Long> {

    // Finds the customer profile linked to a specific user — each user has at most one profile
    Optional<CustomerProfile> findByUserId(Long userId);
}
