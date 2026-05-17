package com.bookstore.modules.user.dto;

/*
 * This is the response DTO for user profile data.
 * It carries the safe, non-sensitive fields of a user returned to the client.
 * Password is intentionally excluded — never sent in any response.
 */

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserResponse {

    // Unique database ID of the user
    private Long id;

    // Display name of the user
    private String name;

    // Email address — also used as the login identifier
    private String email;

    // Role of the user — either ROLE_USER or ROLE_ADMIN
    private String role;

    // Timestamp of when the account was created
    private LocalDateTime createdAt;
}
