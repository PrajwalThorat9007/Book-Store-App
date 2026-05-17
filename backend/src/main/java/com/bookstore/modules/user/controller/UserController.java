package com.bookstore.modules.user.controller;

/*
 * This is the controller layer for the User module.
 * It handles all HTTP requests related to user registration, login, and profile management.
 * Delegates all business logic to UserService.
 * Base URL: /api/users
 */

import com.bookstore.modules.user.dto.AuthResponse;
import com.bookstore.modules.user.dto.LoginRequest;
import com.bookstore.modules.user.dto.RegisterRequest;
import com.bookstore.modules.user.dto.UserResponse;
import com.bookstore.modules.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Registers a new user account — open to everyone, returns a JWT token on success
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = userService.registerUser(request);
        return ResponseEntity.ok(response);
    }

    // Authenticates an existing user — returns a JWT token if credentials are valid
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = userService.loginUser(request);
        return ResponseEntity.ok(response);
    }

    // Returns the profile of the currently logged-in user — requires authentication
    @GetMapping("/profile")
    public ResponseEntity<UserResponse> getProfile() {
        UserResponse profile = userService.getUserProfile();
        return ResponseEntity.ok(profile);
    }

    // Updates the name of the currently logged-in user — email and password changes not handled here
    @PutMapping("/profile")
    public ResponseEntity<UserResponse> updateProfile(@RequestBody UserResponse updateRequest) {
        UserResponse updatedProfile = userService.updateUserProfile(updateRequest);
        return ResponseEntity.ok(updatedProfile);
    }
}
