package com.divum.reimbursement_platform.reimbursementRequest.actions.controller;

import com.divum.reimbursement_platform.commons.entity.SuccessResponse;
import com.divum.reimbursement_platform.reimbursementRequest.actions.dao.ActionRequest;
import com.divum.reimbursement_platform.reimbursementRequest.actions.service.RequestActionsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static com.divum.reimbursement_platform.commons.entity.StatusCode.UPDATED;

@RestController
@RequestMapping("/request/actions")
@RequiredArgsConstructor
@Log4j2
public class RequestActionsController {

    private final RequestActionsService requestActionsService;

    @PostMapping("/{id}")
    public ResponseEntity<SuccessResponse> performActionForManager(@PathVariable("id") final UUID requestId,
                                                                   @RequestBody final ActionRequest actionRequest) {
        log.info("Received request for {} the reimbursement claim {} by manager",
                actionRequest.getStatus().getDescription(), requestId);

        requestActionsService.performActionForManager(requestId, actionRequest);

        return ResponseEntity.ok(new SuccessResponse(requestId.toString(), String.format("Request %s successfully!", actionRequest.getStatus()), UPDATED));
    }

    @PostMapping("/admin/{id}")
    public ResponseEntity<SuccessResponse> performActionForAdmin(@PathVariable("id") final UUID requestId,
                                                                 @RequestBody final ActionRequest actionRequest) {
        log.info("Received request for {} the reimbursement claim {} by admin",
                actionRequest.getStatus().getDescription(), requestId);

        requestActionsService.performActionForAdmin(requestId, actionRequest);

        return ResponseEntity.ok(new SuccessResponse(requestId.toString(), String.format("Request %s successfully!", actionRequest.getStatus()), UPDATED));
    }
}
