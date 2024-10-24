package com.pavlov.first_rest.exception;

/**
 *  кастомный класс для создания своих исключений
 */
public class CustomException extends RuntimeException {
    public CustomException(String message) {
        super(message);
    }
}
