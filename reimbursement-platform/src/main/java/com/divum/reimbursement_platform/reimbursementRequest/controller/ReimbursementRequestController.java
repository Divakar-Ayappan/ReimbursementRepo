package com.divum.reimbursement_platform.reimbursementRequest.controller;

import com.divum.reimbursement_platform.commons.entity.SuccessResponse;
import com.divum.reimbursement_platform.reimbursementRequest.dao.AddReimbursementRequest;
import com.divum.reimbursement_platform.reimbursementRequest.dao.GetRequestsFilter;
import com.divum.reimbursement_platform.reimbursementRequest.dao.UpdateReimbursementRequest;
import jakarta.validation.Valid;
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

import java.util.UUID;

@RestController
@RequestMapping("/request")
public class ReimbursementRequestController {

    @PostMapping
    public ResponseEntity<SuccessResponse> createReimbursementRequest(
            @RequestBody @Valid AddReimbursementRequest addRequest){

        return null;
    }

    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponse> getReimbursementRequestById(@PathVariable final UUID id){
        return null;
    }

    @GetMapping("/employee/{id}")
    public ResponseEntity<SuccessResponse> getReimbursementRequestByEmployeeId(@PathVariable final UUID id,
                                                                               @RequestParam final GetRequestsFilter filter){
        return null;
    }

    @PutMapping("/{id}")
    public ResponseEntity<SuccessResponse> updateReimbursementRequest(@PathVariable final UUID id,
                                                                      @RequestBody @Valid UpdateReimbursementRequest updateRequest){
        return null;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessResponse> cancelReimbursementRequest(@PathVariable final UUID id){
        return null;
    }
}
