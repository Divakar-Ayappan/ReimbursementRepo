package com.divum.reimbursement_platform.commons.exception.entity;


import lombok.Getter;

@Getter
public class EntityAlreadyExistsException extends RuntimeException{

    private final ErrorCode errorCode;
    private final String message;


    public EntityAlreadyExistsException(final String entityName, final String identifier) {
        this.message = String.format("%s with identifier %s already exists!", entityName, identifier);
        this.errorCode = ErrorCode.ENTITY_ALREADY_EXISTS;
    }
}
