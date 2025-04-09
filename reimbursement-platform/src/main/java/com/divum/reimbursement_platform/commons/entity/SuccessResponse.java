package com.divum.reimbursement_platform.commons.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SuccessResponse {
    private String message;
    private StatusCode status;
}
