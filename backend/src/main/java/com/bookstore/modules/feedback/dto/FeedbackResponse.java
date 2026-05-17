package com.bookstore.modules.feedback.dto;

/*
 * This is the response DTO for the Feedback module.
 * It carries the review data returned to the client after submit, edit, or fetch operations.
 * Exposes user name and email for display but never exposes password or role.
 */

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

    // Unique ID of this feedback record — needed by the client for PUT/DELETE calls
    private Long id;

    // Product this review belongs to
    private Long productId;
    private String productTitle;

    // User who submitted the review — shown on the review card in the UI
    private Long userId;
    private String userName;
    private String userEmail;

    // The actual review content
    private Integer rating;
    private String comment;

    // Timestamp set automatically by @PrePersist in the Feedback entity — never set by the user
    private LocalDateTime createdAt;
}
