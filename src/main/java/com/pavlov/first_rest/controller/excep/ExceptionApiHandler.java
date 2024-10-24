package com.pavlov.first_rest.controller.excep;

import com.pavlov.first_rest.exception.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * класс-обработчик исключений
 */
@RestControllerAdvice
public class ExceptionApiHandler {

    /**
     * @param e - кастомное исключение
     * @return объект класса AppError обёрнутый в ResponseEntity содержащий http-код ответа и сообщение
     */
    @ExceptionHandler
    public ResponseEntity<AppError> handleCustomException(CustomException e) {
        return new ResponseEntity<>(new AppError(HttpStatus.NOT_FOUND.value(), e.getMessage()), HttpStatus.NOT_FOUND);
    }
}
