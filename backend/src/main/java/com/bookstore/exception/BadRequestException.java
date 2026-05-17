package com.bookstore.exception;

/**
 * Exception thrown when a bad request is made (e.g., duplicate resource).
 */
public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }
}
