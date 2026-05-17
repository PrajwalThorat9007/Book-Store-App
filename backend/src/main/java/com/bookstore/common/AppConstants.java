package com.bookstore.common;

/*
 * This class holds all application-wide constants used across the bookstore backend.
 * Centralising constants here avoids magic strings/numbers scattered across the codebase.
 * Any new developer can come here to understand the key configuration values in one place.
 * This class cannot be instantiated — all fields are public static final.
 */
public class AppConstants {

    // Private constructor — prevents anyone from creating an instance of this utility class
    private AppConstants() {}

    // ── JWT ──────────────────────────────────────────────────────────────────

    // HTTP header name where the JWT token is expected on every protected request
    public static final String JWT_HEADER = "Authorization";

    // Prefix that comes before the token value in the Authorization header
    public static final String JWT_PREFIX = "Bearer ";

    // Default token validity — 24 hours in milliseconds (matches jwt.expiration in application.properties)
    public static final long JWT_EXPIRATION_MS = 86400000L;

    // ── Roles ────────────────────────────────────────────────────────────────

    // Standard customer role — assigned to every new user on registration
    public static final String ROLE_USER = "ROLE_USER";

    // Admin role — grants access to admin-only endpoints like dashboard and order management
    public static final String ROLE_ADMIN = "ROLE_ADMIN";

    // ── Pagination Defaults ──────────────────────────────────────────────────

    // Default number of items returned per page when no page size is specified
    public static final int DEFAULT_PAGE_SIZE = 10;

    // Maximum allowed page size — prevents clients from requesting huge result sets
    public static final int MAX_PAGE_SIZE = 100;

    // Default field used for sorting when no sort parameter is provided
    public static final String DEFAULT_SORT_BY = "id";

    // Default sort direction — ascending by default
    public static final String DEFAULT_SORT_DIRECTION = "ASC";

    // ── Stock Thresholds ─────────────────────────────────────────────────────

    // Products with stock below this value are flagged as "low stock" in the admin dashboard
    public static final int LOW_STOCK_THRESHOLD = 10;

    // Stock value that means the product is completely out of stock
    public static final int OUT_OF_STOCK = 0;

    // ── API Base Paths ───────────────────────────────────────────────────────

    // Base path prefix for all API endpoints
    public static final String API_BASE = "/api";
}
