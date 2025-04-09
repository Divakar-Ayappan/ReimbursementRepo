package com.divum.reimbursement_platform.commons.exception.entity;

import lombok.Getter;

/**
 * Exception thrown when a requested entity (e.g., Employee, Team, Project) cannot be found in the system.
 *
 * <p>This is a runtime exception intended to be used across various domains where an entity
 * is expected to exist (by ID, name, or other identifier) but does not. It helps encapsulate
 * a standard error message and an {@link ErrorCode} to facilitate consistent error responses.
 *
 * <p>Example usage:
 * <pre>
 *     throw new EntityNotFoundException("Employee", "123e4567-e89b-12d3-a456-426614174000");
 * </pre>
 *
 * <p>The resulting message would be:
 * <pre>
 *     "Employee with identifier 123e4567-e89b-12d3-a456-426614174000 not found"
 * </pre>
 *
 * @author Divakar
 */
@Getter
public class EntityNotFoundException extends RuntimeException {

    private final ErrorCode errorCode;
    private final String message;

    /**
     * Constructs a new {@link EntityNotFoundException} with a formatted message based on the entity name and identifier.
     *
     * @param entityName the name of the missing entity (e.g., "Employee", "Team")
     * @param identifier the value used to search for the entity (e.g., ID, email, name)
     */
    public EntityNotFoundException(final String entityName, final String identifier) {
        this.message = String.format("%s with identifier %s not found", entityName, identifier);
        this.errorCode = ErrorCode.ENTITY_NOT_FOUND;
    }
}

