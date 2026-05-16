package com.bookstore.modules.feedback.controller;

// Manas: Module changed → modules/feedback/controller/FeedbackController.java
// What's changed: Implemented all 7 feedback endpoints — submit, view, rating summary,
//                 edit, delete (user), my-reviews, and admin delete.
//                 Using @RequestParam String email as temp auth until JWT is wired up.

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

    // Manas: Injected via @RequiredArgsConstructor — same pattern used in OrderController
    private final FeedbackService feedbackService;


    // Manas: POST /api/feedback?email=user@example.com
    //        Submit a new review — email param is temp auth until JWT is ready
    //        Returns 201 Created with the saved review in the body
    @PostMapping
    public ResponseEntity<FeedbackResponse> submitFeedback(
            @Valid @RequestBody FeedbackRequest request,
            @RequestParam String email) {

        log.info("POST /api/feedback called by {}", email);
        FeedbackResponse response = feedbackService.submitFeedback(request, email);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    // Manas: GET /api/feedback/product/{productId}
    //        Public endpoint — no email param needed
    //        Returns list of all reviews for the given product
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<FeedbackResponse>> getReviewsByProduct(
            @PathVariable Long productId) {

        log.info("GET /api/feedback/product/{} called", productId);
        List<FeedbackResponse> reviews = feedbackService.getReviewsByProduct(productId);
        return ResponseEntity.ok(reviews);
    }


    // Manas: GET /api/feedback/product/{productId}/summary
    //        Public endpoint — returns average rating + total review count
    //        Frontend uses this to render the star widget on the product page
    @GetMapping("/product/{productId}/summary")
    public ResponseEntity<RatingSummary> getRatingSummary(
            @PathVariable Long productId) {

        log.info("GET /api/feedback/product/{}/summary called", productId);
        RatingSummary summary = feedbackService.getRatingSummary(productId);
        return ResponseEntity.ok(summary);
    }


    // Manas: PUT /api/feedback/{id}?email=user@example.com
    //        User can only edit their own review — ownership check happens in service
    //        Returns 200 OK with the updated review
    @PutMapping("/{id}")
    public ResponseEntity<FeedbackResponse> updateFeedback(
            @PathVariable Long id,
            @Valid @RequestBody FeedbackRequest request,
            @RequestParam String email) {

        log.info("PUT /api/feedback/{} called by {}", id, email);
        FeedbackResponse response = feedbackService.updateFeedback(id, request, email);
        return ResponseEntity.ok(response);
    }


    // Manas: DELETE /api/feedback/{id}?email=user@example.com
    //        User can only delete their own review — ownership check happens in service
    //        Returns 204 No Content on success (nothing to send back after delete)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFeedback(
            @PathVariable Long id,
            @RequestParam String email) {

        log.info("DELETE /api/feedback/{} called by {}", id, email);
        feedbackService.deleteFeedback(id, email);
        return ResponseEntity.noContent().build();
    }


    // Manas: GET /api/feedback/my-reviews?email=user@example.com
    //        Returns all reviews ever submitted by this user
    //        Useful for a "My Reviews" page in the user profile section
    @GetMapping("/my-reviews")
    public ResponseEntity<List<FeedbackResponse>> getMyReviews(
            @RequestParam String email) {

        log.info("GET /api/feedback/my-reviews called by {}", email);
        List<FeedbackResponse> reviews = feedbackService.getMyReviews(email);
        return ResponseEntity.ok(reviews);
    }


    // Manas: DELETE /api/feedback/admin/{id}?email=admin@example.com
    //        Admin can delete any review regardless of who submitted it
    //        No ownership check — admin email is logged for audit trail
    @DeleteMapping("/admin/{id}")
    public ResponseEntity<Void> adminDeleteFeedback(
            @PathVariable Long id,
            @RequestParam String email) {

        log.info("DELETE /api/feedback/admin/{} called by admin {}", id, email);
        feedbackService.adminDeleteFeedback(id, email);
        return ResponseEntity.noContent().build();
    }
}