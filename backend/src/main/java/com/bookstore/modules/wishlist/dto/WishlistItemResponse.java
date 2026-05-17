package com.bookstore.modules.wishlist.dto;

/*
 * This is the response DTO for a single item inside a wishlist.
 * It carries the product details needed to render a wishlist card in the UI.
 * Includes an inStock flag so the frontend can show "Out of Stock" without a separate API call.
 */

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WishlistItemResponse {

    // Unique ID of this wishlist item record
    private Long id;

    // Product details — all the info needed to render the wishlist card
    private Long productId;
    private String productTitle;
    private String productAuthor;
    private String productImageUrl;
    private BigDecimal productPrice;

    // True if stockQuantity > 0 — lets the frontend show "Out of Stock" without a separate call
    private Boolean inStock;
}
