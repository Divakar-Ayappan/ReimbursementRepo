package com.divum.reimbursement_platform.commons.entity;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum StatusCode {

    SUCCESS(200),
    CREATED(201),
    BAD_REQUEST(400),
    NOT_FOUND(404),
    INTERNAL_SERVER_ERROR(500);

    private final Integer code;
}
