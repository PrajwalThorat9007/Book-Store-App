package com.bookstore.modules.category.repository;

/*
 * This is the repository interface for the Category module.
 * It extends JpaRepository to get standard CRUD operations for free.
 * Also defines a custom method to check for duplicate category names before saving.
 */

import com.bookstore.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    // Checks if a category with the given name already exists — used to prevent duplicates
    boolean existsByName(String name);
}
