package com.divum.reimbursement_platform.rules.dao;

import com.divum.reimbursement_platform.rules.entity.RuleCategory;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetRuleResponse {

    private Long ruleId;

    private RuleCategory ruleCategory;

    private Integer reimbursementLimit;

    private Integer autoApprovalLimit;

}
