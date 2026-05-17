package com.bookstore.modules.category.controller;

/*
 * This is the controller layer for the Category module.
 * It handles all incoming HTTP requests related to book categories.
 * Delegates all business logic to CategoryService.
 * Base URL: /api/categories
 */

import com.bookstore.modules.category.dto.CategoryRequest;
import com.bookstore.modules.category.dto.CategoryResponse;
import com.bookstore.modules.category.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    // Returns a list of all available book categories — open to everyone, no auth needed
    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
        List<CategoryResponse> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    // Creates a new category — admin use only, validates request body before processing
    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(@Valid @RequestBody CategoryRequest request) {
        CategoryResponse created = categoryService.createCategory(request);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }
}
