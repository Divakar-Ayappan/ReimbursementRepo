package com.divum.reimbursement_platform.reimbursementRequest.dao;

import com.divum.reimbursement_platform.reimbursementRequest.entity.RejectionReason;
import com.divum.reimbursement_platform.rules.entity.RuleCategory;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class GetReimbursementResponse {

    private UUID requestId;

    private Integer amount;

    private String attachment;

    private String status;

    private RuleCategory ruleCategory;

    private List<LocalDate> claimedDates;

    private List<UUID> employees;

    private LocalDateTime actionDate;

    private RejectionReason rejectionReason;

    private String rejectionComment;

    private String commentByRequester;
}
