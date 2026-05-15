package com.bookstore.modules.feedback.controller;

import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for Feedback operations
 * 
 * TODO: Implement the following endpoints:
 * 
 * PUBLIC endpoints:
 * - GET    /api/feedback/product/{productId}        - Get all reviews for a product
 * - GET    /api/feedback/product/{productId}/summary - Get rating summary
 * 
 * USER endpoints:
 * - POST   /api/feedback                            - Submit new review
 * - PUT    /api/feedback/{id}                       - Update own review
 * - DELETE /api/feedback/{id}                       - Delete own review
 * - GET    /api/feedback/my-reviews                 - Get user's reviews
 * 
 * ADMIN endpoints:
 * - DELETE /api/feedback/admin/{id}                 - Delete any review
 * 
 * TODO: Add security:
 * - Public endpoints: no authentication
 * - @PreAuthorize("hasRole('USER')") on user endpoints
 * - @PreAuthorize("hasRole('ADMIN')") on admin endpoints
 * - Get userId from SecurityContext
 * 
 * TODO: Add validation:
 * - @Valid on request bodies
 * 
 * TODO: Return proper HTTP status codes:
 * - 201 Created for submit review
 * - 200 OK for get operations
 * - 403 Forbidden if trying to edit/delete others' reviews
 */
@RestController
@RequestMapping("/api/feedback")
public class FeedbackController {
    // TODO: Implement feedback controller endpoints
}
