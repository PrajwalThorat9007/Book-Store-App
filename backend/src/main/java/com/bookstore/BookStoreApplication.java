package com.bookstore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main Spring Boot Application
 * Entry point for the Bookstore Backend
 */
@SpringBootApplication
public class BookStoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookStoreApplication.class, args);
        System.out.println("\n==============================================");
        System.out.println("🚀 Bookstore Application Started Successfully!");
        System.out.println("📍 Server running at: http://localhost:8080");
        System.out.println("📊 H2 Console: http://localhost:8080/h2-console");
        System.out.println("==============================================\n");
    }
}
