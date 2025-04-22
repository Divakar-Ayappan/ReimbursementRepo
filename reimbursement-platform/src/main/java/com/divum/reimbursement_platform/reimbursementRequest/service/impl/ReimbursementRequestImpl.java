package com.divum.reimbursement_platform.reimbursementRequest.service.impl;

import com.divum.reimbursement_platform.commons.exception.entity.EntityNotFoundException;
import com.divum.reimbursement_platform.employee.entity.Employee;
import com.divum.reimbursement_platform.employee.repo.EmployeeRepo;
import com.divum.reimbursement_platform.employee.service.EmployeeService;
import com.divum.reimbursement_platform.reimbursementRequest.dao.AddOrEditReimbursementRequest;
import com.divum.reimbursement_platform.reimbursementRequest.dao.GetReimbursementResponse;
import com.divum.reimbursement_platform.reimbursementRequest.dao.GetRequestsFilter;
import com.divum.reimbursement_platform.reimbursementRequest.entity.ClaimedDate;
import com.divum.reimbursement_platform.reimbursementRequest.entity.ReimbursementRequest;
import com.divum.reimbursement_platform.reimbursementRequest.entity.RequestStatus;
import com.divum.reimbursement_platform.reimbursementRequest.repo.ReimbursementRepo;
import com.divum.reimbursement_platform.reimbursementRequest.service.ReimbursementRequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.divum.reimbursement_platform.reimbursementRequest.dao.GetRequestsFilter.PENDING;
import static com.divum.reimbursement_platform.reimbursementRequest.entity.RequestStatus.CANCELLED;

@Log4j2
@Service
@RequiredArgsConstructor
public class ReimbursementRequestImpl implements ReimbursementRequestService {

    final EmployeeService employeeService;

    final EmployeeRepo employeeRepo;

    final ReimbursementRepo reimbursementRepo;

    @Override
    public UUID createReimbursementRequest(final AddOrEditReimbursementRequest addReimbursementRequest) {

        validateReimbursementRequest(addReimbursementRequest);

        final Optional<Employee> requester = employeeRepo.findById(addReimbursementRequest.getEmployeeId());
        if (requester.isEmpty()) {
            throw new EntityNotFoundException("Employee", addReimbursementRequest.getEmployeeId().toString());
        }

        final ReimbursementRequest reimbursementRequest = ReimbursementRequest.builder()
                .requestId(UUID.randomUUID())
                .fromDate(addReimbursementRequest.getFromDate())
                .toDate(addReimbursementRequest.getToDate())
                .employeeId(addReimbursementRequest.getEmployeeId())
                .ruleCategory(addReimbursementRequest.getRuleCategory())
                .amount(addReimbursementRequest.getAmount())
                .attachment(addReimbursementRequest.getAttachment())
                .commentByRequester(addReimbursementRequest.getCommentByRequester())
                .managerId(requester.get().getManagerId())
                .status(RequestStatus.PENDING)
                .build();

        List<Employee> employees = new ArrayList<>();
        if (addReimbursementRequest.getEmployees() != null && !addReimbursementRequest.getEmployees().isEmpty()) {
            employees.addAll(new ArrayList<>(employeeRepo.findAllById(addReimbursementRequest.getEmployees())));
        }

        reimbursementRequest.setEmployees(employees);

        final List<ClaimedDate> claimedDates = addReimbursementRequest.getClaimedDates().stream()
                .map(date -> {
                    ClaimedDate claimedDate = new ClaimedDate();
                    claimedDate.setClaimedDate(date);
                    claimedDate.setReimbursementRequest(reimbursementRequest);
                    return claimedDate;
                })
                .toList();

        reimbursementRequest.setClaimedDates(claimedDates);

        reimbursementRepo.save(reimbursementRequest);

        return reimbursementRequest.getRequestId();
    }

    @Override
    public GetReimbursementResponse getReimbursementRequestById(final UUID reimbursementRequestId) {
        final Optional<ReimbursementRequest> reimbursementRequest = reimbursementRepo.findById(reimbursementRequestId);

        if (reimbursementRequest.isEmpty()) {
            throw new EntityNotFoundException("Reimbursement request", reimbursementRequestId.toString());
        }

        return transformReimbursementToResponse(reimbursementRequest.get());
    }

    @Override
    public List<GetReimbursementResponse> getReimbursementRequestByEmployeeId(final UUID employeeId, final GetRequestsFilter filter) {

        if (Objects.nonNull(filter)) {
            return reimbursementRepo.findAllByEmployeeId(employeeId).stream()
                    .filter(request -> request.getStatus().name().equals(filter.name()))
                    .map(this::transformReimbursementToResponse).toList();
        }

        return reimbursementRepo.findAllByEmployeeId(employeeId).stream()
                .map(this::transformReimbursementToResponse).toList();
    }

    @Override
    public void updateReimbursementRequest(final UUID reimbursementRequestId, final AddOrEditReimbursementRequest updateReimbursementRequest) {

        final Optional<Employee> requester = employeeRepo.findById(updateReimbursementRequest.getEmployeeId());
        if (requester.isEmpty()) {
            throw new EntityNotFoundException("Employee", updateReimbursementRequest.getEmployeeId().toString());
        }

        final Optional<ReimbursementRequest> reimbursementRequest = reimbursementRepo.findById(reimbursementRequestId);
        log.info(requester.get().getEmployeeId() + " " + updateReimbursementRequest.getEmployeeId());

        if (reimbursementRequest.isEmpty()) {
            throw new EntityNotFoundException("Reimbursement request", reimbursementRequestId.toString());
        }

        if (!Objects.equals(reimbursementRequest.get().getStatus(), PENDING)) {
            log.warn("User {} is attempting to update the request {} that is already {}",
                    requester.get().getEmployeeId(),
                    reimbursementRequestId,
                    reimbursementRequest.get().getStatus());
            throw new IllegalArgumentException(String.format("You cannot update this request because it is %s",
                    reimbursementRequest.get().getStatus()));
        }

        if (!Objects.equals(requester.get().getEmployeeId(), reimbursementRequest.get().getEmployeeId())) {
            throw new IllegalArgumentException("Employee cannot be changed!");
        }


        final ReimbursementRequest reimbursementRequestToUpdate = reimbursementRequest.get();

        reimbursementRequestToUpdate.setFromDate(updateReimbursementRequest.getFromDate());
        reimbursementRequestToUpdate.setToDate(updateReimbursementRequest.getToDate());
        reimbursementRequestToUpdate.setRuleCategory(updateReimbursementRequest.getRuleCategory());
        reimbursementRequestToUpdate.setAmount(updateReimbursementRequest.getAmount());
        reimbursementRequestToUpdate.setAttachment(updateReimbursementRequest.getAttachment());
        reimbursementRequestToUpdate.setCommentByRequester(updateReimbursementRequest.getCommentByRequester());

        // Clear existing claimed dates
        reimbursementRequestToUpdate.getClaimedDates().clear();

        // Add new claimed dates to the existing collection
        updateReimbursementRequest.getClaimedDates().forEach(date -> {
            ClaimedDate claimedDate = new ClaimedDate();
            claimedDate.setClaimedDate(date);
            claimedDate.setReimbursementRequest(reimbursementRequestToUpdate);
            reimbursementRequestToUpdate.getClaimedDates().add(claimedDate);
        });

        if (updateReimbursementRequest.getEmployees() != null && !updateReimbursementRequest.getEmployees().isEmpty()) {
            reimbursementRequestToUpdate.setEmployees(new ArrayList<>(employeeRepo.findAllById(updateReimbursementRequest.getEmployees())));
        }
        reimbursementRequestToUpdate.setEmployees(reimbursementRequest.get().getEmployees());
        reimbursementRepo.save(reimbursementRequestToUpdate);
    }

    @Override
    public void cancelReimbursementRequest(final UUID reimbursementRequestId) {
        final Optional<ReimbursementRequest> reimbursementRequest = reimbursementRepo.findById(reimbursementRequestId);

        if (reimbursementRequest.isEmpty()) {
            throw new EntityNotFoundException("Reimbursement request", reimbursementRequestId.toString());
        }
        reimbursementRequest.get().setStatus(CANCELLED);

        reimbursementRepo.save(reimbursementRequest.get());
    }
    
    private GetReimbursementResponse transformReimbursementToResponse(final ReimbursementRequest reimbursementRequest) {
        log.info("Transforming ReimbursementRequest to response for request: {}", reimbursementRequest );
        return GetReimbursementResponse.builder()
                .requestId(reimbursementRequest.getRequestId())
                .amount(reimbursementRequest.getAmount())
                .attachment(reimbursementRequest.getAttachment())
                .status(reimbursementRequest.getStatus().toString())
                .ruleCategory(reimbursementRequest.getRuleCategory())
                .claimedDates(reimbursementRequest.getClaimedDates().stream().map
                        (ClaimedDate::getClaimedDate).toList())
                .employees(reimbursementRequest.getEmployees().stream().map(Employee::getEmployeeId).toList())
                .actionDate(reimbursementRequest.getActionDate())
                .rejectionReason(reimbursementRequest.getRejectedReason())
                .rejectionComment(reimbursementRequest.getCommentByManager())
                .commentByRequester(reimbursementRequest.getCommentByRequester())
                .build();
    }

    private void validateReimbursementRequest(final AddOrEditReimbursementRequest addOrEditReimbursementRequest) {
        if (addOrEditReimbursementRequest.getFromDate().isAfter(addOrEditReimbursementRequest.getToDate())) {
            throw new IllegalArgumentException("From date cannot be after to date");
        }

        for (LocalDate claimedDate : addOrEditReimbursementRequest.getClaimedDates()) {
            if (claimedDate.isBefore(addOrEditReimbursementRequest.getFromDate()) ||
                    claimedDate.isAfter(addOrEditReimbursementRequest.getToDate())) {
                throw new IllegalArgumentException("Claimed date must be between from date and to date");
            }
        }
    }
}
