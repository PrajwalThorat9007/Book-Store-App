package com.bookstore.modules.product.service;

/*
 * This is the service interface for the Product module.
 * It defines the contract for all product-related business operations.
 * The actual logic lives in ProductServiceImpl.
 */

import com.bookstore.modules.product.dto.ProductRequest;
import com.bookstore.modules.product.dto.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {

    // Returns a paginated list of all products — supports sorting by any field
    Page<ProductResponse> getAllProducts(Pageable pageable);

    // Returns a single product by its ID — throws ResourceNotFoundException if not found
    ProductResponse getProductById(Long id);

    // Searches products by title, author, or category name — case-insensitive, paginated
    Page<ProductResponse> searchProducts(String query, Pageable pageable);

    // Creates a new product listing — validates ISBN uniqueness and category existence
    ProductResponse createProduct(ProductRequest request);

    // Updates an existing product — validates ISBN uniqueness if changed, checks category exists
    ProductResponse updateProduct(Long id, ProductRequest request);

    // Deletes a product by ID — throws ResourceNotFoundException if not found
    void deleteProduct(Long id);
}
