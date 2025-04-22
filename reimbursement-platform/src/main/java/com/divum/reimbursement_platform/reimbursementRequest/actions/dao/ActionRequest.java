package com.divum.reimbursement_platform.reimbursementRequest.actions.dao;

import com.divum.reimbursement_platform.reimbursementRequest.entity.RejectionReason;
import com.divum.reimbursement_platform.reimbursementRequest.entity.RequestStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ActionRequest {

    @NotNull
    private RequestStatus status;

    private String comment;

    private RejectionReason rejectionReason;

}
