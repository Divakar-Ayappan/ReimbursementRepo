package com.divum.reimbursement_platform.rules.service;

import com.divum.reimbursement_platform.employee.dto.GetRulesFilter;
import com.divum.reimbursement_platform.rules.dao.AddOrUpdateRuleRequest;
import com.divum.reimbursement_platform.rules.dao.GetRuleResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RulesService {

    Long createRule(final AddOrUpdateRuleRequest addRuleRequest);

    void updateRule(final Long ruleId, final AddOrUpdateRuleRequest updateRuleRequest);

    GetRuleResponse getRule(final Long ruleId);

    List<GetRuleResponse> getAllRules(final GetRulesFilter rulesFilter);

    void deactivateRule(final Long ruleId);
}
