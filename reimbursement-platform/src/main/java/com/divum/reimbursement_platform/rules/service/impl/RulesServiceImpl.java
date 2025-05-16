package com.divum.reimbursement_platform.rules.service.impl;

import com.divum.reimbursement_platform.commons.exception.entity.EntityAlreadyExistsException;
import com.divum.reimbursement_platform.commons.exception.entity.EntityNotFoundException;
import com.divum.reimbursement_platform.employee.dto.GetRulesFilter;
import com.divum.reimbursement_platform.rules.dao.AddOrUpdateRuleRequest;
import com.divum.reimbursement_platform.rules.dao.GetRuleResponse;
import com.divum.reimbursement_platform.rules.entity.Rules;
import com.divum.reimbursement_platform.rules.repo.RulesRepo;
import com.divum.reimbursement_platform.rules.service.RulesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.divum.reimbursement_platform.commons.utility.Utils.defaultIfNull;

@Service
@RequiredArgsConstructor
@Log4j2
public class RulesServiceImpl implements RulesService {

    private final RulesRepo rulesRepo;


    @Override
    public Long createRule(final AddOrUpdateRuleRequest addRuleRequest) {
        if (Objects.nonNull(rulesRepo.findByCategory(addRuleRequest.getRuleCategory()))) {
            throw new EntityAlreadyExistsException("Rule", addRuleRequest.getRuleCategory().name());
        }

        final Rules rules = Rules.builder()
                .category(addRuleRequest.getRuleCategory())
                .reimbursementLimit(addRuleRequest.getReimbursementLimit())
                .autoApprovalLimit(defaultIfNull(addRuleRequest.getAutoApprovalLimit(), 0))
                .isActive(true)
                .ruleDescription(addRuleRequest.getRuleDescription())
                .build();

        log.info("Creating rule: {}", rules);
        rulesRepo.save(rules);

        return rulesRepo.findByCategory(addRuleRequest.getRuleCategory()).getId();
    }

    @Override
    public void updateRule(final Long ruleId, final AddOrUpdateRuleRequest updateRuleRequest) {
        final Rules rules = rulesRepo.findById(ruleId).orElseThrow(() ->
                new EntityNotFoundException("Rule", ruleId.toString()));

        if(!Objects.equals(rules.getCategory(), updateRuleRequest.getRuleCategory())) {
            throw new IllegalArgumentException("Rule category cannot be updated. Please create a new rule if required.");
        }

        rules.setReimbursementLimit(updateRuleRequest.getReimbursementLimit());
        rules.setAutoApprovalLimit(defaultIfNull(updateRuleRequest.getAutoApprovalLimit(), 0));
        rules.setActive(defaultIfNull(updateRuleRequest.getIsActive(), rules.isActive()));
        rules.setRuleDescription(defaultIfNull(updateRuleRequest.getRuleDescription(),
                String.format("This is the rule for %s. And will be auto approved if the claim is less than %s",
                        rules.getCategory(), updateRuleRequest.getAutoApprovalLimit())));

        log.info("Updating rule: {}", rules);
        rulesRepo.save(rules);
    }

    @Override
    public List<GetRuleResponse> getAllRules(final GetRulesFilter rulesFilter) {

        return switch (rulesFilter) {
            case ALL -> rulesRepo.findAll().stream()
                    .map(this::transformRuleToResponse)
                    .collect(Collectors.toList());
            case ACTIVE -> rulesRepo.findAll().stream()
                    .filter(Rules::isActive)
                    .map(this::transformRuleToResponse)
                    .collect(Collectors.toList());
            case INACTIVE -> rulesRepo.findAll().stream()
                    .filter(rule -> !rule.isActive())
                    .map(this::transformRuleToResponse)
                    .collect(Collectors.toList());
            default -> new ArrayList<>();
        };
    }

    @Override
    public GetRuleResponse getRule(final Long ruleId) {
        log.info("Getting rule with id: {}", ruleId);

        final Rules rule = rulesRepo.findById(ruleId).orElseThrow(() ->
                new EntityNotFoundException("Rule", ruleId.toString()));

        return transformRuleToResponse(rule);
    }

    private GetRuleResponse transformRuleToResponse(final Rules rule) {
        return GetRuleResponse.builder()
                .ruleId(rule.getId())
                .ruleCategory(rule.getCategory())
                .reimbursementLimit(rule.getReimbursementLimit())
                .autoApprovalLimit(rule.getAutoApprovalLimit())
                .description(rule.getRuleDescription() + getAutoApprovalTextDescription(rule.getAutoApprovalLimit()))
                .build();
    }

    private static String getAutoApprovalTextDescription(final Integer autoApprovalLimit) {
        if(autoApprovalLimit > 0) {
            return String.format(" This request will be auto approved if the claimed amount is less than ₹%s", autoApprovalLimit);
        }
        return "";
    }


    @Override
    public void deactivateRule(final Long ruleId) {
        final Rules rule = rulesRepo.findById(ruleId).orElseThrow(() ->
                new EntityNotFoundException("Rule", ruleId.toString()));

        rule.setActive(false);
        log.info("Deactivating rule: {}", rule);
        rulesRepo.save(rule);
    }
}
