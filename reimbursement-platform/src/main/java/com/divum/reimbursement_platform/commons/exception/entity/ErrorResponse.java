package com.divum.reimbursement_platform.commons.exception.entity;

import lombok.Data;

import java.time.Instant;

import lombok.Data;
import java.time.Instant;
import java.util.Map;

/**
 * Represents a standardized structure for sending error responses to the client.
 *
 * <p>This class is used in exception handling to encapsulate essential information
 * about an error in a predictable, frontend-friendly format. It includes a user-readable
 * message, an error code for programmatic identification, and a timestamp indicating when
 * the error occurred.
 *
 * <p>Typical usage is within a {@code @ControllerAdvice} to wrap exceptions into a uniform response.
 *
 * <p>Example output:
 * <pre>
 * {
 *   "message": "Employee with identifier 1234 not found",
 *   "errorCode": "EMPLOYEE_NOT_FOUND",
 *   "timestamp": "2025-04-09T14:32:11.012Z"
 * }
 * </pre>
 *
 * <p>This makes it easier for frontend applications and API consumers to handle errors consistently.
 */
@Data
public class ErrorResponse {

    /** A human-readable message explaining the error. */
    private final String message;

    /** A custom application-level error code used for categorizing errors. */
    private final String errorCode;

    /** The timestamp when the error occurred (in UTC). */
    private final Instant timestamp;

    private final Map<String, String> fieldErrors;

    /**
     * Constructs a new {@code ErrorResponse} with the given message and error code.
     * The timestamp is automatically set to the current time.
     *
     * @param message   the human-readable error message
     * @param errorCode the application-defined error code
     */
    public ErrorResponse(String message, String errorCode) {
        this.message = message;
        this.errorCode = errorCode;
        this.timestamp = Instant.now();
        this.fieldErrors = null;
    }

    public ErrorResponse(String message, String errorCode, Map<String, String> fieldErrors) {
        this.message = message;
        this.errorCode = errorCode;
        this.timestamp = Instant.now();
        this.fieldErrors = fieldErrors;
    }
}
