package com.bookstore.modules.feedback.dto;

// Manas: Module changed → modules/feedback/dto/FeedbackResponse.java
// What's changed: Created FeedbackResponse DTO — the outgoing payload returned
//                 to the client after submit, edit, or when fetching reviews.
//                 Maps from Feedback entity without exposing internal JPA objects.

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackResponse {

    // Manas: The feedback entry's own ID — needed by frontend for PUT/DELETE calls
    private Long id;

    // Manas: Product details — client needs to know which product this review belongs to
    private Long productId;
    private String productTitle;

    // Manas: User details — shown on review card (e.g. "Reviewed by Manas")
    //        We expose name + email but NOT password, role, or any sensitive user fields
    private Long userId;
    private String userName;
    private String userEmail;

    // Manas: The actual review content
    private Integer rating;
    private String comment;

    // Manas: Timestamp — shown as "Reviewed on May 17, 2026" on the frontend
    //        Set automatically by @PrePersist in the Feedback entity, never by the user
    private LocalDateTime createdAt;
}