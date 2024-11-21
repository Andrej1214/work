package com.pavlov.first_rest.exception;

/**
 *  кастомный класс для создания своих исключений
 */
public class CustomNotFoundException extends RuntimeException {
    public CustomNotFoundException(String message) {
        super(message);
    }
}
