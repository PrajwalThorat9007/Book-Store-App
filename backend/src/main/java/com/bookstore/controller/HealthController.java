package com.bookstore.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Health Check Controller
 * Simple endpoint to verify the application is running
 */
@RestController
@RequestMapping("/api")
public class HealthController {

    @GetMapping("/health")
    public Map<String, Object> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("message", "Bookstore Backend is running successfully!");
        response.put("timestamp", LocalDateTime.now());
        response.put("version", "0.0.1-SNAPSHOT");
        return response;
    }

    @GetMapping("/")
    public Map<String, String> welcome() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Welcome to Bookstore API");
        response.put("health", "/api/health");
        response.put("h2Console", "/h2-console");
        response.put("documentation", "See IMPLEMENTATION_GUIDE.md for details");
        return response;
    }
}
