package com.bookstore.modules.category.service;

/*
 * This is the service interface for the Category module.
 * It defines the contract for all category-related business operations.
 * The actual logic lives in CategoryServiceImpl.
 */

import com.bookstore.modules.category.dto.CategoryRequest;
import com.bookstore.modules.category.dto.CategoryResponse;

import java.util.List;

public interface CategoryService {

    // Fetch all categories from the database and return them as a list of response DTOs
    List<CategoryResponse> getAllCategories();

    // Create a new category after checking for duplicate names — admin only operation
    CategoryResponse createCategory(CategoryRequest request);
}
