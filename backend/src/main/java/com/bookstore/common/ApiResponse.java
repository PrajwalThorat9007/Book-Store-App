package com.bookstore.common;

/**
 * Generic API Response wrapper
 * 
 * TODO: Implement as a record or class with the following fields:
 * - boolean success
 * - String message
 * - T data (generic type)
 * 
 * TODO: Add static factory methods:
 * - ApiResponse<T> success(T data)
 * - ApiResponse<T> success(String message, T data)
 * - ApiResponse<Void> error(String message)
 * 
 * Example usage:
 * return ResponseEntity.ok(ApiResponse.success("User created", userResponse));
 * return ResponseEntity.badRequest().body(ApiResponse.error("Invalid input"));
 */
public class ApiResponse<T> {
    // TODO: Implement API response wrapper
}
