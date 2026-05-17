package com.bookstore.modules.product.dto;

/*
 * This is the request DTO for the Product module.
 * It carries the data sent by the client when creating or updating a book listing.
 * Validation annotations ensure all required fields are present and values are sensible.
 */

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductRequest {

    // Book title — required field, shown in listings and search results
    @NotBlank(message = "Title is required")
    private String title;

    // Author name — required, displayed on the product card
    @NotBlank(message = "Author is required")
    private String author;

    // ISBN is optional but must be unique if provided — used to prevent duplicate listings
    private String isbn;

    // Price must be a positive value — BigDecimal used to avoid floating point precision issues
    @NotNull(message = "Price is required")
    @Positive(message = "Price must be greater than zero")
    private BigDecimal price;

    // Stock quantity must be zero or more — zero means out of stock
    @NotNull(message = "Stock quantity is required")
    @PositiveOrZero(message = "Stock quantity cannot be negative")
    private Integer stockQuantity;

    // Optional URL for the book cover image shown in the UI
    private String imageUrl;

    // Category the book belongs to — must reference an existing category ID
    @NotNull(message = "Category ID is required")
    private Long categoryId;
}
