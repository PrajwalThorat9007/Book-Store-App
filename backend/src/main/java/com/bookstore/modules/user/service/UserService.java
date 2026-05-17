package com.bookstore.modules.user.service;

/*
 * This is the service layer class for the User module.
 * It contains all business logic for user registration, login, and profile management.
 * Handles password encoding, duplicate email checks, and reading the authenticated user from SecurityContext.
 */

import com.bookstore.common.AppConstants;
import com.bookstore.entity.User;
import com.bookstore.modules.user.dto.AuthResponse;
import com.bookstore.modules.user.dto.LoginRequest;
import com.bookstore.modules.user.dto.RegisterRequest;
import com.bookstore.modules.user.dto.UserResponse;
import com.bookstore.modules.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Registers a new user — checks for duplicate email, hashes the password, saves to DB
    public AuthResponse registerUser(RegisterRequest request) {
        // Reject registration if the email is already taken
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email is already taken!");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        // Always store hashed password — never plain text
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(AppConstants.ROLE_USER);

        userRepository.save(user);

        // TODO: Replace with real JWT token generation using JwtUtil
        String token = "mock-jwt-token-for-now";

        return new AuthResponse(token, "User registered successfully");
    }

    // Validates email and password, returns a JWT token if credentials match
    public AuthResponse loginUser(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password!"));

        // Compare the raw password against the stored BCrypt hash
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password!");
        }

        // TODO: Replace with real JWT token generation using JwtUtil
        String token = "mock-jwt-token-for-now";

        return new AuthResponse(token, "Login successful");
    }

    // Reads the authenticated user's email from SecurityContext and returns their profile
    public UserResponse getUserProfile() {
        String email = getAuthenticatedUserEmail();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found!"));
        return mapToUserResponse(user);
    }

    // Updates the user's name — email and password updates are intentionally excluded here
    public UserResponse updateUserProfile(UserResponse updateRequest) {
        String email = getAuthenticatedUserEmail();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        // Only update name if a non-empty value is provided
        if (updateRequest.getName() != null && !updateRequest.getName().isEmpty()) {
            user.setName(updateRequest.getName());
        }

        User updatedUser = userRepository.save(user);
        return mapToUserResponse(updatedUser);
    }

    // Extracts the logged-in user's email from the Spring Security context
    private String getAuthenticatedUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new RuntimeException("User not authenticated!");
        }
        return authentication.getName();
    }

    // Converts a User entity to a UserResponse DTO — never exposes password or sensitive fields
    private UserResponse mapToUserResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());
        response.setCreatedAt(user.getCreatedAt());
        return response;
    }
}
