package com.bookstore.modules.feedback.service;

/*
 * This is the service layer class for the Feedback module.
 * It contains all business logic for submitting, editing, deleting, and fetching product reviews.
 * Enforces the one-review-per-user-per-product rule and ownership checks on edit/delete.
 * Admin delete bypasses ownership checks — any review can be removed by an admin.
 */

import com.bookstore.entity.Feedback;
import com.bookstore.entity.Product;
import com.bookstore.entity.User;
import com.bookstore.modules.feedback.dto.FeedbackRequest;
import com.bookstore.modules.feedback.dto.FeedbackResponse;
import com.bookstore.modules.feedback.dto.RatingSummary;
import com.bookstore.modules.feedback.repository.FeedbackRepository;
import com.bookstore.modules.product.repository.ProductRepository;
import com.bookstore.modules.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    // Submits a new review — checks for duplicate review before hitting the DB unique constraint
    @Transactional
    public FeedbackResponse submitFeedback(FeedbackRequest request, String email) {
        log.info("User {} is submitting a review for product {}", email, request.getProductId());

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + request.getProductId()));

        // Reject if the user has already reviewed this product — gives a clean error before the DB constraint fires
        boolean alreadyReviewed = feedbackRepository
                .findByUserIdAndProductId(user.getId(), product.getId())
                .isPresent();

        if (alreadyReviewed) {
            throw new RuntimeException("You have already reviewed this product. Use edit instead.");
        }

        Feedback feedback = new Feedback();
        feedback.setUser(user);
        feedback.setProduct(product);
        feedback.setRating(request.getRating());
        feedback.setComment(request.getComment());

        Feedback saved = feedbackRepository.save(feedback);
        log.info("Review saved with id {} for product {} by user {}", saved.getId(), product.getId(), email);

        return mapToResponse(saved);
    }

    // Returns all reviews for a product — public, no auth needed
    @Transactional(readOnly = true)
    public List<FeedbackResponse> getReviewsByProduct(Long productId) {
        log.info("Fetching all reviews for product {}", productId);

        return feedbackRepository.findByProductId(productId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Calculates average rating and review count — returns 0.0 average if no reviews exist
    @Transactional(readOnly = true)
    public RatingSummary getRatingSummary(Long productId) {
        log.info("Fetching rating summary for product {}", productId);

        Double avg = feedbackRepository.findAverageRatingByProductId(productId);
        long count = feedbackRepository.countByProductId(productId);

        // JPQL AVG returns null when there are no rows — default to 0.0 to avoid NPE
        double roundedAvg = (avg == null) ? 0.0 : Math.round(avg * 10.0) / 10.0;

        return RatingSummary.builder()
                .productId(productId)
                .averageRating(roundedAvg)
                .totalReviews(count)
                .hasReviews(count > 0)
                .build();
    }

    // Updates an existing review — verifies the review belongs to the requesting user before editing
    @Transactional
    public FeedbackResponse updateFeedback(Long feedbackId, FeedbackRequest request, String email) {
        log.info("User {} is attempting to edit review {}", email, feedbackId);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        Feedback feedback = feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new RuntimeException("Review not found with id: " + feedbackId));

        // Ownership check — users can only edit their own reviews
        if (!feedback.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You are not allowed to edit someone else's review.");
        }

        feedback.setRating(request.getRating());
        feedback.setComment(request.getComment());

        Feedback updated = feedbackRepository.save(feedback);
        log.info("Review {} updated successfully by user {}", feedbackId, email);

        return mapToResponse(updated);
    }

    // Deletes a review — verifies ownership before deleting, users cannot delete others' reviews
    @Transactional
    public void deleteFeedback(Long feedbackId, String email) {
        log.info("User {} is attempting to delete review {}", email, feedbackId);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        Feedback feedback = feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new RuntimeException("Review not found with id: " + feedbackId));

        // Ownership check — same rule as update, users cannot delete other users' reviews
        if (!feedback.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You are not allowed to delete someone else's review.");
        }

        feedbackRepository.delete(feedback);
        log.info("Review {} deleted by user {}", feedbackId, email);
    }

    // Returns all reviews submitted by a specific user — used in the "My Reviews" profile page
    @Transactional(readOnly = true)
    public List<FeedbackResponse> getMyReviews(String email) {
        log.info("Fetching all reviews submitted by user {}", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        return feedbackRepository.findByUserId(user.getId())
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Admin-only delete — no ownership check, admin can remove any review
    @Transactional
    public void adminDeleteFeedback(Long feedbackId, String adminEmail) {
        log.info("Admin {} is deleting review {}", adminEmail, feedbackId);

        Feedback feedback = feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new RuntimeException("Review not found with id: " + feedbackId));

        feedbackRepository.delete(feedback);
        log.info("Review {} deleted by admin {}", feedbackId, adminEmail);
    }

    // Converts a Feedback entity to a FeedbackResponse DTO — called by every method that returns data
    private FeedbackResponse mapToResponse(Feedback feedback) {
        return FeedbackResponse.builder()
                .id(feedback.getId())
                .productId(feedback.getProduct().getId())
                .productTitle(feedback.getProduct().getTitle())
                .userId(feedback.getUser().getId())
                .userName(feedback.getUser().getName())
                .userEmail(feedback.getUser().getEmail())
                .rating(feedback.getRating())
                .comment(feedback.getComment())
                .createdAt(feedback.getCreatedAt())
                .build();
    }
}
