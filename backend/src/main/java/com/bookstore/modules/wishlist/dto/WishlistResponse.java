package com.bookstore.modules.wishlist.dto;

/*
 * This is the response DTO for the Wishlist module.
 * Returned after any wishlist operation — get, add, or remove.
 * Includes the full list of saved items and a total count for easy display.
 */

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WishlistResponse {

    // Unique database ID of the wishlist record
    private Long id;

    // ID of the user who owns this wishlist
    private Long userId;

    // All products currently saved in the wishlist
    private List<WishlistItemResponse> items;

    // Total number of items — convenient for displaying a badge count in the UI
    private Integer totalItems;
}
