package com.bookstore.modules.user.dto;

/*
 * This is the request DTO for the user registration endpoint.
 * It carries the name, email, and password submitted during sign-up.
 * Validation annotations enforce required fields, email format, and minimum password length.
 */

import lombok.Data;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
public class RegisterRequest {

    // Full name of the user — required, stored as-is in the database
    @NotBlank(message = "Name is required")
    private String name;

    // Email is used as the unique login identifier — must be a valid format
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    // Password must be at least 6 characters — will be BCrypt hashed before saving
    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;
}
