package com.bookstore.modules.user.dto;

/*
 * This is the request DTO for the user login endpoint.
 * It carries the email and password submitted by the user on the login form.
 * Validation annotations ensure both fields are present and the email format is valid.
 */

import lombok.Data;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Data
public class LoginRequest {

    // Must be a valid email format — used to look up the user in the database
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    // Raw password submitted by the user — compared against the stored BCrypt hash in service
    @NotBlank(message = "Password is required")
    private String password;
}
