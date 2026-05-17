package com.bookstore.modules.category.service;

import com.bookstore.modules.category.dto.CategoryRequest;
import com.bookstore.modules.category.dto.CategoryResponse;

import java.util.List;

/**
 * Service interface for Category operations.
 */
public interface CategoryService {

    /**
     * Get all categories.
     */
    List<CategoryResponse> getAllCategories();

    /**
     * Create a new category (Admin only).
     */
    CategoryResponse createCategory(CategoryRequest request);
}
