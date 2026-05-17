package com.bookstore.modules.feedback.controller;

/*
 * This is the controller layer for the Feedback module.
 * It handles all HTTP requests related to product reviews and ratings.
 * Uses ?email= query param as temporary auth until JWT is fully wired up.
 * Delegates all business logic to FeedbackService.
 * Base URL: /api/feedback
 */

import com.bookstore.modules.feedback.dto.FeedbackRequest;
import com.bookstore.modules.feedback.dto.FeedbackResponse;
import com.bookstore.modules.feedback.dto.RatingSummary;
import com.bookstore.modules.feedback.service.FeedbackService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/feedback")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;

    // Submits a new review for a product — one review per user per product is enforced in service
    @PostMapping
    public ResponseEntity<FeedbackResponse> submitFeedback(
            @Valid @RequestBody FeedbackRequest request,
            @RequestParam String email) {

        log.info("POST /api/feedback called by {}", email);
        FeedbackResponse response = feedbackService.submitFeedback(request, email);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Returns all reviews for a specific product — public endpoint, no auth needed
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<FeedbackResponse>> getReviewsByProduct(
            @PathVariable Long productId) {

        log.info("GET /api/feedback/product/{} called", productId);
        List<FeedbackResponse> reviews = feedbackService.getReviewsByProduct(productId);
        return ResponseEntity.ok(reviews);
    }

    // Returns the average rating and total review count for a product — used to render the star widget
    @GetMapping("/product/{productId}/summary")
    public ResponseEntity<RatingSummary> getRatingSummary(
            @PathVariable Long productId) {

        log.info("GET /api/feedback/product/{}/summary called", productId);
        RatingSummary summary = feedbackService.getRatingSummary(productId);
        return ResponseEntity.ok(summary);
    }

    // Updates an existing review — service verifies the review belongs to the requesting user
    @PutMapping("/{id}")
    public ResponseEntity<FeedbackResponse> updateFeedback(
            @PathVariable Long id,
            @Valid @RequestBody FeedbackRequest request,
            @RequestParam String email) {

        log.info("PUT /api/feedback/{} called by {}", id, email);
        FeedbackResponse response = feedbackService.updateFeedback(id, request, email);
        return ResponseEntity.ok(response);
    }

    // Deletes a review — service verifies the review belongs to the requesting user
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFeedback(
            @PathVariable Long id,
            @RequestParam String email) {

        log.info("DELETE /api/feedback/{} called by {}", id, email);
        feedbackService.deleteFeedback(id, email);
        return ResponseEntity.noContent().build();
    }

    // Returns all reviews submitted by a specific user — used in the "My Reviews" profile section
    @GetMapping("/my-reviews")
    public ResponseEntity<List<FeedbackResponse>> getMyReviews(
            @RequestParam String email) {

        log.info("GET /api/feedback/my-reviews called by {}", email);
        List<FeedbackResponse> reviews = feedbackService.getMyReviews(email);
        return ResponseEntity.ok(reviews);
    }

    // Admin-only delete — removes any review regardless of who submitted it
    @DeleteMapping("/admin/{id}")
    public ResponseEntity<Void> adminDeleteFeedback(
            @PathVariable Long id,
            @RequestParam String email) {

        log.info("DELETE /api/feedback/admin/{} called by admin {}", id, email);
        feedbackService.adminDeleteFeedback(id, email);
        return ResponseEntity.noContent().build();
    }
}
