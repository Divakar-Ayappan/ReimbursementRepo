package com.divum.reimbursement_platform.reimbursementRequest.controller;

import com.divum.reimbursement_platform.commons.entity.SuccessResponse;
import com.divum.reimbursement_platform.reimbursementRequest.dao.AddOrEditReimbursementRequest;
import com.divum.reimbursement_platform.reimbursementRequest.dao.GetReimbursementResponse;
import com.divum.reimbursement_platform.reimbursementRequest.dao.GetRequestsFilter;
import com.divum.reimbursement_platform.reimbursementRequest.service.ReimbursementRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
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
import java.util.UUID;

import static com.divum.reimbursement_platform.commons.entity.StatusCode.CANCELLED;
import static com.divum.reimbursement_platform.commons.entity.StatusCode.CREATED;
import static com.divum.reimbursement_platform.commons.entity.StatusCode.UPDATED;

@Log4j2
@RestController
@RequestMapping("/request")
@RequiredArgsConstructor
public class ReimbursementRequestController {

    private final ReimbursementRequestService reimbursementRequestService;

    @PostMapping
    public ResponseEntity<SuccessResponse> createReimbursementRequest(
            @RequestBody @Valid AddOrEditReimbursementRequest addRequest){
        log.info("Request received to create reimbursement request: {}", addRequest);

        final UUID requestId = reimbursementRequestService.createReimbursementRequest(addRequest);

        return ResponseEntity.ok(new SuccessResponse(requestId.toString(), "Request created successfully!", CREATED));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetReimbursementResponse> getReimbursementRequestById(@PathVariable final UUID id){
        log.info("Request received to get reimbursement request by id: {}", id);

        final GetReimbursementResponse response = reimbursementRequestService.getReimbursementRequestById(id);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/employee/{id}")
    public ResponseEntity<List<GetReimbursementResponse>> getReimbursementRequestByEmployeeId(
            @PathVariable final UUID id,
            @RequestParam(value = "filter", required = false) final GetRequestsFilter filter){

        log.info("Request received to get reimbursement request by employee id: {}", id);

        final List<GetReimbursementResponse> reimbursementResponses = reimbursementRequestService
                .getReimbursementRequestByEmployeeId(id, filter);

        return ResponseEntity.ok(reimbursementResponses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SuccessResponse> updateReimbursementRequest(@PathVariable final UUID id,
                                                                      @RequestBody @Valid AddOrEditReimbursementRequest updateRequest){
        log.info("Request received to update reimbursement request: {}", updateRequest);
        reimbursementRequestService.updateReimbursementRequest(id, updateRequest);

        return ResponseEntity.ok(new SuccessResponse(id.toString(),"Request updated successfully!", UPDATED));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessResponse> cancelReimbursementRequest(@PathVariable final UUID id){
        log.info("Request received to cancel reimbursement request: {}", id);

        reimbursementRequestService.cancelReimbursementRequest(id);

        return ResponseEntity.ok(new SuccessResponse(id.toString(), "Request cancelled successfully!", CANCELLED));
    }
}
