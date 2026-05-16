package com.bookstore.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// Manas: Module changed → common/ApiResponse.java
// What's changed: Was an empty TODO stub, now fully implemented with static factory methods
// Used by CartController and other controllers as a standard response wrapper

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {

    private boolean success;
    private String message;
    private T data;

    // Manas: success(data) — for when you just want to return data with no custom message
    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .message("Success")
                .data(data)
                .build();
    }

    // Manas: success(message, data) — for when you want a custom message like "Item added to cart"
    public static <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .build();
    }

    // Manas: error(message) — for returning error responses with no data payload
    public static <T> ApiResponse<T> error(String message) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .data(null)
                .build();
    }
}