package com.bookstore.modules.product.dto;

/*
 * This is the response DTO for the Product module.
 * It carries the book data returned to the client after create, update, or fetch operations.
 * Includes flattened category info so the client doesn't need a separate category API call.
 */

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductResponse {

    // Unique database ID of the product
    private Long id;

    // Book title
    private String title;

    // Author name
    private String author;

    // ISBN — may be null if not provided during creation
    private String isbn;

    // Price of the book
    private BigDecimal price;

    // Current stock quantity — zero means out of stock
    private Integer stockQuantity;

    // URL of the book cover image — may be null
    private String imageUrl;

    // ID of the category this book belongs to
    private Long categoryId;

    // Name of the category — flattened here so the client doesn't need a second API call
    private String categoryName;
}
