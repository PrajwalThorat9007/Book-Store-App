package com.bookstore.modules.feedback.dto;

/*
 * This is the request DTO for the Feedback module.
 * Used for both submitting a new review (POST) and editing an existing one (PUT).
 * Validation annotations enforce rating range (1–5) and comment length limit.
 */

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class FeedbackRequest {

    // ID of the product being reviewed — required on submit, ignored on edit (product cannot be changed)
    @NotNull(message = "Product ID is required")
    private Long productId;

    // Star rating — must be between 1 and 5 inclusive
    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    private Integer rating;

    // Optional written review — capped at 2000 characters to prevent abuse
    @Size(max = 2000, message = "Comment must not exceed 2000 characters")
    private String comment;
}
