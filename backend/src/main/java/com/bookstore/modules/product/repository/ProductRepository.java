package com.bookstore.modules.product.repository;

import com.bookstore.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// TODO: Add more custom query methods as needed during Product module implementation
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategoryId(Long categoryId);
    boolean existsByIsbn(String isbn);
}
