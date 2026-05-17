package com.bookstore.modules.user.controller;

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

    /**
     * Public endpoint to create a user account.
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = userService.registerUser(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Public endpoint to login and receive a JWT token.
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = userService.loginUser(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Protected endpoint to view profile (USER/ADMIN).
     */
    @GetMapping("/profile")
    public ResponseEntity<UserResponse> getProfile() {
        UserResponse profile = userService.getUserProfile();
        return ResponseEntity.ok(profile);
    }

    /**
     * Protected endpoint to update profile (USER/ADMIN).
     */
    @PutMapping("/profile")
    public ResponseEntity<UserResponse> updateProfile(@RequestBody UserResponse updateRequest) {
        UserResponse updatedProfile = userService.updateUserProfile(updateRequest);
        return ResponseEntity.ok(updatedProfile);
    }
}
