package com.divum.reimbursement_platform.rules.controller;

import com.divum.reimbursement_platform.commons.entity.SuccessResponse;
import com.divum.reimbursement_platform.employee.dto.GetRulesFilter;
import com.divum.reimbursement_platform.rules.dao.AddOrUpdateRuleRequest;
import com.divum.reimbursement_platform.rules.dao.GetRuleResponse;
import com.divum.reimbursement_platform.rules.service.RulesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.divum.reimbursement_platform.commons.entity.StatusCode.CREATED;
import static com.divum.reimbursement_platform.commons.entity.StatusCode.DEACTIVATED;
import static com.divum.reimbursement_platform.commons.entity.StatusCode.UPDATED;

@RestController
@RequestMapping("/rules")
@RequiredArgsConstructor
@Log4j2
@CrossOrigin
public class RulesController {

    private final RulesService rulesService;


    @PostMapping
    public ResponseEntity<SuccessResponse> createRule(@RequestBody @Valid final AddOrUpdateRuleRequest addRuleRequest) {
        log.info(" Received request for creating rule: {}", addRuleRequest);

        final Long id = rulesService.createRule(addRuleRequest);
        return ResponseEntity.ok(new SuccessResponse(id.toString(), "Rule created successfully", CREATED));
    }

    @GetMapping
    public ResponseEntity<List<GetRuleResponse>> getAllRules(@RequestParam(value = "rulesFilter", defaultValue = "true")
                                                             final GetRulesFilter rulesFilter) {
        log.info("Received request for getting {} rules", rulesFilter);

        final List<GetRuleResponse> rules = rulesService.getAllRules(rulesFilter);
        return ResponseEntity.ok(rules);
    }

    @GetMapping("/{ruleId}")
    public ResponseEntity<GetRuleResponse> getRule(@PathVariable final Long ruleId) {
        log.info("Received request for getting rule with id: {}", ruleId);

        final GetRuleResponse rule = rulesService.getRule(ruleId);
        return ResponseEntity.ok(rule);
    }

    @PutMapping("/{ruleId}")
    public ResponseEntity<SuccessResponse> updateRule(@PathVariable final Long ruleId, @RequestBody @Valid AddOrUpdateRuleRequest addOrUpdateRuleRequest) {
        log.info("Received request for updating rule with id: {}", ruleId);

        rulesService.updateRule(ruleId, addOrUpdateRuleRequest);
        return ResponseEntity.ok(new SuccessResponse("Rule updated successfully", UPDATED));
    }

    @DeleteMapping("/{ruleId}/deactivate")
    public ResponseEntity<SuccessResponse> deactivateRule(@PathVariable final Long ruleId) {
        log.info("Received request for deactivating rule with id: {}", ruleId);

        rulesService.deactivateRule(ruleId);
        return ResponseEntity.ok(new SuccessResponse("Rule deactivated successfully", DEACTIVATED));
    }
}

