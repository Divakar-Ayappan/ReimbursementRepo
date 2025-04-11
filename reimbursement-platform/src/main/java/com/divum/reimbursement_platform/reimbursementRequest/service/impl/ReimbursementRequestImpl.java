package com.divum.reimbursement_platform.reimbursementRequest.service.impl;

import com.divum.reimbursement_platform.commons.exception.entity.EntityNotFoundException;
import com.divum.reimbursement_platform.employee.dao.GetEmployeeResponse;
import com.divum.reimbursement_platform.employee.entity.Employee;
import com.divum.reimbursement_platform.employee.service.EmployeeService;
import com.divum.reimbursement_platform.projects.dao.GetProjectResponse;
import com.divum.reimbursement_platform.projects.entity.Project;
import com.divum.reimbursement_platform.projects.repo.ProjectRepo;
import com.divum.reimbursement_platform.projects.service.ProjectService;
import com.divum.reimbursement_platform.reimbursementRequest.dao.AddReimbursementRequest;
import com.divum.reimbursement_platform.reimbursementRequest.dao.GetReimbursementResponse;
import com.divum.reimbursement_platform.reimbursementRequest.dao.GetRequestsFilter;
import com.divum.reimbursement_platform.reimbursementRequest.entity.ReimbursementRequest;
import com.divum.reimbursement_platform.reimbursementRequest.service.ReimbursementRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReimbursementRequestImpl implements ReimbursementRequestService {

    final EmployeeService employeeService;

    final ProjectService projectService;

    @Override
    public UUID createReimbursementRequest(final AddReimbursementRequest addReimbursementRequest) {

        final GetEmployeeResponse requester = employeeService.getEmployee(addReimbursementRequest.getEmployeeId());

        final GetProjectResponse projectResponse = projectService.getProjectById(requester.getProjectId());


        if (addReimbursementRequest.getFromDate().isAfter(addReimbursementRequest.getToDate())) {
            throw new IllegalArgumentException("From date cannot be after to date");
        }

        for  (LocalDate date : addReimbursementRequest.getRequestingDates()) {
            if (date.isBefore(addReimbursementRequest.getFromDate()) || date.isAfter(addReimbursementRequest.getToDate())) {
                throw new IllegalArgumentException("Requesting date must be between from date and to date");
            }
        }


        if (addReimbursementRequest.getEmployees() != null) {
            for (UUID employeeId : addReimbursementRequest.getEmployees()) {
                employeeService.getEmployee(employeeId);
//                try {
//                    employeeService.getEmployee(employeeId);
//                }catch (EntityNotFoundException  e) {
//                    throw new IllegalArgumentException("Employee not found");
//                }
            }
        }


        final ReimbursementRequest  reimbursementRequest = ReimbursementRequest.builder()
                .requestId(UUID.randomUUID())
                .fromDate(addReimbursementRequest.getFromDate())
                .toDate(addReimbursementRequest.getToDate())
                .employeeId(addReimbursementRequest.getEmployeeId())
//                .projectId()
                .ruleCategory(addReimbursementRequest.getRuleCategory())
                .amount(addReimbursementRequest.getAmount())
                .attachment(addReimbursementRequest.getAttachment())
                .commentByRequester(addReimbursementRequest.getCommentByRequester())
                .build();

        return null;
    }

    @Override
    public GetReimbursementResponse getReimbursementRequestById(UUID reimbursementRequestId) {
        return null;
    }

    @Override
    public List<GetReimbursementResponse> getReimbursementRequestByEmployeeId(UUID employeeId, GetRequestsFilter filter) {
        return List.of();
    }

    @Override
    public void updateReimbursementRequest(UUID reimbursementRequestId, AddReimbursementRequest addReimbursementRequest) {

    }

    @Override
    public void cancelReimbursementRequest(UUID reimbursementRequestId) {

    }
}
