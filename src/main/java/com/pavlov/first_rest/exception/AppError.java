package com.pavlov.first_rest.exception;

import lombok.*;
import lombok.experimental.FieldDefaults;


/**
 * класс для создания исключений нужного вида(сождержащего статус код и читабельное сообщение) в RestController-e
 */
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Setter
@Getter
public class AppError {
    int statusCode;
    String message;
}
