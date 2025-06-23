package com.divum.reimbursement_platform.commons.exception;

import com.divum.reimbursement_platform.commons.exception.entity.EntityAlreadyExistsException;
import com.divum.reimbursement_platform.commons.exception.entity.EntityNotFoundException;
import com.divum.reimbursement_platform.commons.exception.entity.ErrorResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

import static com.divum.reimbursement_platform.commons.exception.entity.ErrorCode.INVALID_REQUEST;
import static com.divum.reimbursement_platform.commons.exception.entity.ErrorCode.VALIDATIONS_FAILED;

@Log4j2
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
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        ErrorResponse errorResponse = new ErrorResponse("Validations failed for some fields", VALIDATIONS_FAILED.toString(),
                errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }


    /**
     * Handles {@link EntityNotFoundException} and returns a structured {@link ErrorResponse}
     * with a 404 NOT FOUND status.
     *
     * <p>This ensures that when any entity like Employee, Team, etc., is not found,
     * the API responds with a clear, predictable structure.
     *
     * @param ex the exception thrown when an entity is not found
     * @return a {@link ResponseEntity} containing the error details
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityAlreadyExistsException ex) {
        log.info("Entering Exception Handler for EntityAlreadyExistsException");
        log.info("Error: " +ex.getMessage() + " Stack trace: " + ex.getStackTrace());
        final ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), ex.getErrorCode().toString());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    /**
     * Handles {@link EntityAlreadyExistsException} and returns a structured {@link ErrorResponse}
     * with a 400 BAD REQUEST status.
     *
     * <p>This ensures that when any entity, such as Employee, Team, etc., is already present but an attempt
     * is made to add it again, the API responds with a clear, predictable structure, indicating the conflict.
     *
     * @param ex the exception thrown when an entity is already present
     * @return a {@link ResponseEntity} containing the error details
     */
    @ExceptionHandler(EntityAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleEntityAlreadyExistsException(EntityAlreadyExistsException ex) {
        log.info("Entering Exception Handler for EntityAlreadyExistsException");
        log.info("Error: " +ex.getMessage() + " Stack trace: " + ex.getStackTrace());
        final ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), ex.getErrorCode().toString());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalAccessException(IllegalArgumentException ex) {
        log.error("IllegalArgumentException caught in GlobalExceptionHandler", ex);
        final ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), INVALID_REQUEST.toString());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}

