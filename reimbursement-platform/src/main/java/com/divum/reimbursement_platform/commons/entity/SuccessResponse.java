package com.divum.reimbursement_platform.commons.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
public class SuccessResponse {
    private String id;
    private String message;
    private StatusCode status;

    public SuccessResponse(final String message, final StatusCode status) {
        this.message = message;
        this.status = status;
    }
}
