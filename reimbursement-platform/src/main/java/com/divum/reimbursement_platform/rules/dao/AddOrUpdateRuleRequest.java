package com.divum.reimbursement_platform.rules.dao;

import com.divum.reimbursement_platform.rules.entity.RuleCategory;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddOrUpdateRuleRequest {

    @NotNull(message = "Rule category is required")
    private RuleCategory ruleCategory;

    @Min(value = 1, message = "Limit must be at least 1")
    @Max(value = 100000, message = "Limit cannot exceed 100000")
    @NotNull(message = "ReimbursementLimit is required")
    private Integer reimbursementLimit;

    @Min(value = 1, message = "Limit must be at least 1")
    @Max(value = 100000, message = "Limit cannot exceed 100000")
    private Integer autoApprovalLimit = 0;

    private Boolean isActive;

    private String ruleDescription;
}
