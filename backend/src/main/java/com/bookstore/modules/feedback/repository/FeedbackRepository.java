package com.bookstore.modules.feedback.repository;

/*
 * This is the repository interface for the Feedback module.
 * It extends JpaRepository to get standard CRUD operations for free.
 * Provides custom queries for fetching reviews by product or user,
 * calculating average ratings, and checking review ownership.
 */

import com.bookstore.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    // Returns all reviews for a specific product — used in the product detail page
    List<Feedback> findByProductId(Long productId);

    // Checks if a user has already reviewed a product — enforces one review per user per product
    Optional<Feedback> findByUserIdAndProductId(Long userId, Long productId);

    // Calculates the average rating for a product — returns null if no reviews exist yet
    @Query("SELECT AVG(f.rating) FROM Feedback f WHERE f.product.id = :productId")
    Double findAverageRatingByProductId(@Param("productId") Long productId);

    // Counts total reviews for a product — used alongside average rating in the RatingSummary response
    long countByProductId(Long productId);

    // Checks if a feedback entry belongs to a specific user — used for ownership validation in PUT/DELETE
    boolean existsByIdAndUserId(Long feedbackId, Long userId);

    // Returns all reviews submitted by a specific user — used in the "My Reviews" profile page
    List<Feedback> findByUserId(Long userId);
}
