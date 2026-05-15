package com.bookstore.modules.feedback.repository;

import com.bookstore.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Repository for Feedback entity
 * 
 * TODO: Implement the following methods:
 * - List<Feedback> findByProductIdOrderByCreatedAtDesc(Long productId)
 * - List<Feedback> findByUserIdOrderByCreatedAtDesc(Long userId)
 * - Optional<Feedback> findByUserIdAndProductId(Long userId, Long productId)
 * - @Query("SELECT AVG(f.rating) FROM Feedback f WHERE f.product.id = :productId")
 *   Double calculateAverageRating(@Param("productId") Long productId)
 * - Long countByProductId(Long productId)
 */
@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    // TODO: Add custom query methods
}
