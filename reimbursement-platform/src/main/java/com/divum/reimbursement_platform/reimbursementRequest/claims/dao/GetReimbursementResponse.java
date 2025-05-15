package com.divum.reimbursement_platform.reimbursementRequest.claims.dao;

import com.divum.reimbursement_platform.employee.entity.Role;
import com.divum.reimbursement_platform.reimbursementRequest.entity.RejectionReason;
import com.divum.reimbursement_platform.rules.entity.RuleCategory;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
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

    private LocalDate createdAt;

    private LocalDate fromDate;

    private LocalDate toDate;

    private Role pendingWith;
}
