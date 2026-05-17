package com.bookstore.modules.user.repository;

/*
 * This is the repository interface for the User module.
 * It extends JpaRepository to get standard CRUD operations for free.
 * Provides custom methods to look up users by email and check for duplicate emails.
 */

import com.bookstore.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Finds a user by their email address — used during login and profile fetch
    Optional<User> findByEmail(String email);

    // Checks if an email is already registered — used during registration to prevent duplicates
    boolean existsByEmail(String email);
}
