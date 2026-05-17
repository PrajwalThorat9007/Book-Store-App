package com.bookstore.modules.feedback.dto;

/*
 * This is the response DTO for the product rating summary.
 * Returned by GET /api/feedback/product/{id}/summary.
 * Gives the frontend everything it needs to render a star rating widget.
 */

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RatingSummary {

    // The product this summary belongs to
    private Long productId;

    // Average rating rounded to 1 decimal place — defaults to 0.0 if no reviews exist
    private Double averageRating;

    // Total number of reviews submitted for this product
    private Long totalReviews;

    // Convenience flag — true if at least one review exists, false otherwise
    private Boolean hasReviews;
}
