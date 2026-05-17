package com.bookstore.modules.product.repository;

/*
 * This is the repository interface for the Product module.
 * It extends JpaRepository to get standard CRUD and pagination operations for free.
 * Also defines custom methods for ISBN uniqueness checks, category filtering, and full-text search.
 */

import com.bookstore.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // Checks if a product with the given ISBN already exists — used to prevent duplicate listings
    boolean existsByIsbn(String isbn);

    // Returns all products belonging to a specific category with pagination support
    Page<Product> findByCategoryId(Long categoryId, Pageable pageable);

    // Case-insensitive search across title, author, and category name — supports partial matches
    @Query("SELECT p FROM Product p JOIN p.category c WHERE " +
           "LOWER(p.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(p.author) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(c.name) LIKE LOWER(CONCAT('%', :query, '%'))")
    Page<Product> searchProducts(@Param("query") String query, Pageable pageable);
}
