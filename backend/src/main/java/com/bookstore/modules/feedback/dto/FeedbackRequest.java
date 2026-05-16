package com.bookstore.modules.feedback.dto;

// Manas: Module changed → modules/feedback/dto/FeedbackRequest.java
// What's changed: Created FeedbackRequest DTO used for both POST (submit review)
//                 and PUT (edit review) endpoints. Contains full validation annotations.

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class FeedbackRequest {

    // Manas: productId is required on POST (submit new review)
    //        On PUT (edit review), this field is ignored in service logic —
    //        user cannot change which product they reviewed after submission
    @NotNull(message = "Product ID is required")
    private Long productId;

    // Manas: Rating must be between 1 and 5 (inclusive)
    //        @Min and @Max enforce this at the controller layer before service is called
    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    private Integer rating;

    // Manas: Comment is optional (nullable = true in entity)
    //        But if provided, we cap it at 2000 characters to prevent abuse
    @Size(max = 2000, message = "Comment must not exceed 2000 characters")
    private String comment;
}