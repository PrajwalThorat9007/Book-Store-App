package com.bookstore.modules.feedback.dto;

// Manas: Module changed → modules/feedback/dto/RatingSummary.java
// What's changed: Created RatingSummary DTO — returned by GET /api/feedback/product/{id}/rating
//                 Gives the frontend everything it needs to render a star rating widget:
//                 the average score and the total number of reviews.

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RatingSummary {

    // Manas: The product this summary belongs to
    //        Frontend can use this to confirm it got the right product's data
    private Long productId;

    // Manas: Average rating rounded to 1 decimal place (e.g. 4.3, not 4.285714...)
    //        Null-safe — will be 0.0 if no reviews exist yet (handled in service)
    private Double averageRating;

    // Manas: Total number of reviews submitted for this product
    //        Displayed as "4.3 ★ (128 reviews)" on the product page
    private Long totalReviews;

    // Manas: Convenience flag for the frontend —
    //        Instead of checking totalReviews == 0, frontend can just check hasReviews
    private Boolean hasReviews;
}