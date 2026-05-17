package com.bookstore.modules.category.service.impl;

/*
 * This is the service implementation class for the Category module.
 * It contains the actual business logic for creating and fetching categories.
 * Implements CategoryService interface and is injected into CategoryController.
 */

import com.bookstore.entity.Category;
import com.bookstore.exception.BadRequestException;
import com.bookstore.modules.category.dto.CategoryRequest;
import com.bookstore.modules.category.dto.CategoryResponse;
import com.bookstore.modules.category.repository.CategoryRepository;
import com.bookstore.modules.category.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    // Fetches all categories from DB and maps each entity to a response DTO
    @Override
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Checks for duplicate name first, then saves the new category to DB
    @Override
    public CategoryResponse createCategory(CategoryRequest request) {
        // Reject if a category with the same name already exists
        if (categoryRepository.existsByName(request.getName())) {
            throw new BadRequestException("Category with name '" + request.getName() + "' already exists");
        }

        Category category = new Category();
        category.setName(request.getName());
        category.setDescription(request.getDescription());

        Category savedCategory = categoryRepository.save(category);
        return mapToResponse(savedCategory);
    }

    // Converts a Category entity into a CategoryResponse DTO for the API response
    private CategoryResponse mapToResponse(Category category) {
        CategoryResponse response = new CategoryResponse();
        response.setId(category.getId());
        response.setName(category.getName());
        response.setDescription(category.getDescription());
        return response;
    }
}
