package com.bookstore.modules.category.dto;

/*
 * This is the request DTO for the Category module.
 * It carries the data sent by the client when creating a new category.
 * Validation annotations ensure name is not blank before the request reaches the service.
 */

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoryRequest {

    // Category name is mandatory — used as a unique identifier for the category
    @NotBlank(message = "Category name is required")
    private String name;

    // Optional description to give more context about what books belong in this category
    private String description;
}
