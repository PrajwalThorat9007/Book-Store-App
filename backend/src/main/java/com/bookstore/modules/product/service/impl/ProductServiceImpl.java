package com.bookstore.modules.product.service.impl;

/*
 * This is the service implementation class for the Product module.
 * It contains all business logic for creating, updating, deleting, and searching book listings.
 * Implements ProductService and is injected into ProductController.
 */

import com.bookstore.entity.Category;
import com.bookstore.entity.Product;
import com.bookstore.exception.BadRequestException;
import com.bookstore.exception.ResourceNotFoundException;
import com.bookstore.modules.category.repository.CategoryRepository;
import com.bookstore.modules.product.dto.ProductRequest;
import com.bookstore.modules.product.dto.ProductResponse;
import com.bookstore.modules.product.repository.ProductRepository;
import com.bookstore.modules.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    // Fetches all products with pagination — maps each entity to a response DTO
    @Override
    public Page<ProductResponse> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(this::mapToResponse);
    }

    // Finds a single product by ID — throws 404 if not found
    @Override
    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", id));
        return mapToResponse(product);
    }

    // Delegates search to the repository's JPQL query — returns paginated results
    @Override
    public Page<ProductResponse> searchProducts(String query, Pageable pageable) {
        return productRepository.searchProducts(query, pageable)
                .map(this::mapToResponse);
    }

    // Creates a new product — checks ISBN uniqueness and validates the category exists
    @Override
    public ProductResponse createProduct(ProductRequest request) {
        // Reject if another product already has this ISBN
        if (request.getIsbn() != null && !request.getIsbn().isBlank()
                && productRepository.existsByIsbn(request.getIsbn())) {
            throw new BadRequestException("A product with ISBN '" + request.getIsbn() + "' already exists");
        }

        // Category must exist before we can assign it to the product
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", request.getCategoryId()));

        Product product = new Product();
        product.setTitle(request.getTitle());
        product.setAuthor(request.getAuthor());
        product.setIsbn(request.getIsbn());
        product.setPrice(request.getPrice());
        product.setStockQuantity(request.getStockQuantity());
        product.setImageUrl(request.getImageUrl());
        product.setCategory(category);

        Product savedProduct = productRepository.save(product);
        return mapToResponse(savedProduct);
    }

    // Updates an existing product — only checks ISBN uniqueness if the ISBN has changed
    @Override
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", id));

        // Only validate ISBN if it's being changed to a new value
        if (request.getIsbn() != null && !request.getIsbn().isBlank()
                && !request.getIsbn().equals(product.getIsbn())
                && productRepository.existsByIsbn(request.getIsbn())) {
            throw new BadRequestException("A product with ISBN '" + request.getIsbn() + "' already exists");
        }

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", request.getCategoryId()));

        product.setTitle(request.getTitle());
        product.setAuthor(request.getAuthor());
        product.setIsbn(request.getIsbn());
        product.setPrice(request.getPrice());
        product.setStockQuantity(request.getStockQuantity());
        product.setImageUrl(request.getImageUrl());
        product.setCategory(category);

        Product updatedProduct = productRepository.save(product);
        return mapToResponse(updatedProduct);
    }

    // Deletes a product by ID — throws 404 if the product doesn't exist
    @Override
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", id));
        productRepository.delete(product);
    }

    // Converts a Product entity to a ProductResponse DTO — flattens category name into the response
    private ProductResponse mapToResponse(Product product) {
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setTitle(product.getTitle());
        response.setAuthor(product.getAuthor());
        response.setIsbn(product.getIsbn());
        response.setPrice(product.getPrice());
        response.setStockQuantity(product.getStockQuantity());
        response.setImageUrl(product.getImageUrl());
        response.setCategoryId(product.getCategory().getId());
        response.setCategoryName(product.getCategory().getName());
        return response;
    }
}
