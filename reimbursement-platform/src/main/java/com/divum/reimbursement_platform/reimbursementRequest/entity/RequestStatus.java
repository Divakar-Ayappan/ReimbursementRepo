package com.divum.reimbursement_platform.reimbursementRequest.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RequestStatus {
    PENDING("Pending"),
    APPROVED("Approve"),
    REJECTED("Reject"),
    CANCELLED("Cancel");

    private final String description;
}
