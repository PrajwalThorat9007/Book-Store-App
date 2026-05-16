package com.bookstore.modules.feedback.repository;

// Manas: Module changed → modules/feedback/repository/FeedbackRepository.java
// What's changed: Created FeedbackRepository with custom queries for
//                 fetching reviews by product and calculating average rating.

import com.bookstore.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    // Manas: Get all feedback entries for a specific product (used in GET /api/feedback/product/{id})
    List<Feedback> findByProductId(Long productId);

    // Manas: Check if a user has already reviewed a product
    //        Used in service to enforce the "one review per user per product" business rule
    //        (DB unique constraint is the hard stop, this gives a clean error message before hitting it)
    Optional<Feedback> findByUserIdAndProductId(Long userId, Long productId);

    // Manas: Calculate the average rating for a product
    //        Returns null if no reviews exist yet — service handles the null case
    @Query("SELECT AVG(f.rating) FROM Feedback f WHERE f.product.id = :productId")
    Double findAverageRatingByProductId(@Param("productId") Long productId);

    // Manas: Count total number of reviews for a product
    //        Used alongside average rating to build the RatingSummary DTO response
    long countByProductId(Long productId);

    // Manas: Check if a specific feedback entry belongs to a specific user
    //        Used in PUT and DELETE to verify ownership before allowing edits/deletes
    boolean existsByIdAndUserId(Long feedbackId, Long userId);
    List<Feedback> findByUserId(Long userId);
}