package com.bookstore.modules.user.service;

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

    public AuthResponse registerUser(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email is already taken!");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole("ROLE_USER"); // Default role

        userRepository.save(user);

        // TODO: Generate real JWT token using JwtUtil
        String token = "mock-jwt-token-for-now";
        
        return new AuthResponse(token, "User registered successfully");
    }

    public AuthResponse loginUser(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password!"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password!");
        }

        // TODO: Generate real JWT token using JwtUtil
        String token = "mock-jwt-token-for-now";

        return new AuthResponse(token, "Login successful");
    }

    public UserResponse getUserProfile() {
        String email = getAuthenticatedUserEmail();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found!"));
        return mapToUserResponse(user);
    }

    public UserResponse updateUserProfile(UserResponse updateRequest) {
        String email = getAuthenticatedUserEmail();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        if (updateRequest.getName() != null && !updateRequest.getName().isEmpty()) {
            user.setName(updateRequest.getName());
        }
        
        // Note: Not updating email or password here for simplicity, 
        // usually email updates require verification.
        
        User updatedUser = userRepository.save(user);
        return mapToUserResponse(updatedUser);
    }

    private String getAuthenticatedUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new RuntimeException("User not authenticated!");
        }
        return authentication.getName();
    }

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
