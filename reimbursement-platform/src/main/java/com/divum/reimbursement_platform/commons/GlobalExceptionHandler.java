package com.divum.reimbursement_platform.commons;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles validation errors triggered by the {@code @Valid} annotation in controller methods.
     *
     * <p>This method catches {@link MethodArgumentNotValidException}, which occurs when validation
     * constraints (e.g., {@code @NotBlank}, {@code @Email}, {@code @Size}) on a request DTO fail.
     *
     * <p>It transforms the default verbose Spring Boot error into a simplified JSON response
     * mapping each invalid field to its corresponding error message, making it more frontend-friendly.
     *
     * <p><b>✅ Example output when this handler exists:</b>
     * <pre>
     * {
     *   "firstName": "First name is required",
     *   "email": "Invalid email format"
     * }
     * </pre>
     *
     * <p><b>❌ Example output when this handler is removed (Spring Boot's default):</b>
     * <pre>
     * {
     *     "timestamp": "2025-04-08T18:04:51.063+00:00",
     *     "status": 400,
     *     "error": "Bad Request",
     *     "path": "/employee/"
     * }
     * </pre>
     * This includes a stack trace and is not suitable for exposing to frontend or external clients.
     *
     * <p><b>Why use this handler?</b><br>
     * To return clean, predictable, and user-friendly validation responses and avoid leaking internal details.
     *
     * @param ex the exception thrown due to validation failure
     * @return a response entity containing field-to-error-message mapping with HTTP 400 status
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}

