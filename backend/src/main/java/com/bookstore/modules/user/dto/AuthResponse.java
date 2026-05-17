package com.bookstore.modules.user.dto;

/*
 * This is the response DTO returned after a successful login or registration.
 * It carries the JWT token the client must include in future requests,
 * and a human-readable message confirming the operation result.
 */

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {

    // JWT token the client stores and sends in the Authorization header for protected endpoints
    private String token;

    // Confirmation message shown to the user, e.g. "Login successful" or "User registered successfully"
    private String message;
}
