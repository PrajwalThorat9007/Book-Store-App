package com.bookstore.common;

/**
 * Application-wide constants
 * 
 * TODO: Define the following constants:
 * 
 * JWT Configuration:
 * - String JWT_SECRET (load from application.properties)
 * - long JWT_EXPIRATION_MS (e.g., 86400000L for 24 hours)
 * - String JWT_HEADER = "Authorization"
 * - String JWT_PREFIX = "Bearer "
 * 
 * Role Names:
 * - String ROLE_USER = "USER"
 * - String ROLE_ADMIN = "ADMIN"
 * 
 * Pagination:
 * - int DEFAULT_PAGE_SIZE = 10
 * - int MAX_PAGE_SIZE = 100
 * - String DEFAULT_SORT_BY = "id"
 * - String DEFAULT_SORT_DIRECTION = "ASC"
 * 
 * Stock Thresholds:
 * - int LOW_STOCK_THRESHOLD = 10
 * - int OUT_OF_STOCK = 0
 */
public class AppConstants {
    // TODO: Implement application constants
    
    private AppConstants() {
        // Private constructor to prevent instantiation
    }
}
