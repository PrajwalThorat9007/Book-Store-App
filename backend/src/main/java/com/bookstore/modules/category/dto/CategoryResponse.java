package com.bookstore.modules.category.dto;

/*
 * This is the response DTO for the Category module.
 * It carries the category data returned to the client after create or fetch operations.
 * Only exposes safe fields — no internal JPA entity is ever sent directly to the client.
 */

import lombok.Data;

@Data
public class CategoryResponse {

    // Unique database ID of the category
    private Long id;

    // Display name of the category shown in the UI
    private String name;

    // Optional description of the category
    private String description;
}
