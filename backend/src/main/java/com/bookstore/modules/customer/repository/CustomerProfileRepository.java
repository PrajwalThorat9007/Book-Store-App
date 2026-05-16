package com.bookstore.modules.customer.repository;

import com.bookstore.entity.CustomerProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for CustomerProfile entity.
 *
 * This repository handles all database operations related to
 * customer profile management.
 *
 * JpaRepository already provides built-in methods like:
 * - save()
 * - findById()
 * - findAll()
 * - deleteById()
 *
 * We can also create custom query methods using
 * Spring Data JPA method naming conventions.
 */
@Repository
public interface CustomerProfileRepository extends JpaRepository<CustomerProfile, Long> {

    /**
     * Finds a customer profile using the associated user id.
     *
     * This is useful because each customer profile
     * is linked to exactly one user.
     *
     * Spring Data JPA automatically converts this method into SQL.
     *
     * Relationship path:
     * CustomerProfile -> User -> id
     *
     * @param userId id of the user
     * @return optional customer profile
     */
    Optional<CustomerProfile> findByUserId(Long userId);
}