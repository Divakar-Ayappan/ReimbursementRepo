package com.divum.reimbursement_platform.reimbursementRequest.actions.service.impl;

import com.divum.reimbursement_platform.async.request.events.factory.AdminEventFactory;
import com.divum.reimbursement_platform.async.request.events.factory.ManagerEventFactory;
import com.divum.reimbursement_platform.commons.exception.entity.EntityNotFoundException;
import com.divum.reimbursement_platform.reimbursementRequest.actions.dao.ActionRequest;
import com.divum.reimbursement_platform.reimbursementRequest.actions.service.RequestActionsService;
import com.divum.reimbursement_platform.reimbursementRequest.entity.ReimbursementRequest;
import com.divum.reimbursement_platform.reimbursementRequest.entity.RequestStatus;
import com.divum.reimbursement_platform.reimbursementRequest.repo.ReimbursementRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import static com.divum.reimbursement_platform.employee.entity.Role.ADMIN;
import static com.divum.reimbursement_platform.employee.entity.Role.EMPLOYEE;
import static com.divum.reimbursement_platform.reimbursementRequest.entity.RejectionReason.OTHERS;
import static com.divum.reimbursement_platform.reimbursementRequest.entity.RequestStatus.CANCELLED;
import static com.divum.reimbursement_platform.reimbursementRequest.entity.RequestStatus.PENDING;
import static com.divum.reimbursement_platform.reimbursementRequest.entity.RequestStatus.REJECTED;

@Service
@RequiredArgsConstructor
@Log4j2
public class RequestActionsServiceImpl implements RequestActionsService {

    private final ReimbursementRepo reimbursementRepo;

    private final ApplicationEventPublisher eventPublisher;

    private final ManagerEventFactory managerEventFactory;

    private final AdminEventFactory adminEventFactory;

    @Override
    @Transactional
    public void performActionForManager(final UUID requestId, final ActionRequest actionRequest) {

        final ReimbursementRequest request = reimbursementRepo.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("Reimbursement Request", requestId.toString()));

        if (!Objects.equals(request.getStatus(), PENDING)) {
            throw new IllegalArgumentException(String.format("You cannot %s a request that is already %s",
                    actionRequest.getStatus().getDescription(), request.getStatus()));
        }

        validateActionRequestForRejection(actionRequest, request);

        request.setRejectedReason(actionRequest.getRejectionReason());
        request.setCommentByManager(actionRequest.getComment());
        request.setStatus(actionRequest.getStatus());
        request.setActionDate(LocalDateTime.now());
        request.setPendingWith(actionRequest.getStatus().equals(REJECTED) ? EMPLOYEE : ADMIN);

        reimbursementRepo.save(request);

        final ApplicationEvent event = managerEventFactory.getEventForManager(this, request);
        eventPublisher.publishEvent(event);
    }


    @Override
    @Transactional
    public void performActionForAdmin(UUID requestId, ActionRequest actionRequest) {
        final ReimbursementRequest request = reimbursementRepo.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("Reimbursement Request", requestId.toString()));

        if (Objects.equals(request.getStatus(), CANCELLED)) {
            throw new IllegalArgumentException(String.format("You cannot %s a request that is already %s",
                    actionRequest.getStatus().getDescription(), request.getStatus()));
        }

        validateActionRequestForRejection(actionRequest, request);

        request.setRejectedReason(actionRequest.getRejectionReason());
        request.setCommentByAdmin(actionRequest.getComment());
        request.setStatus(actionRequest.getStatus());
        request.setActionDate(LocalDateTime.now());
        request.setPendingWith(EMPLOYEE);

        reimbursementRepo.save(request);

        final ApplicationEvent event = adminEventFactory.getEventForAdmin(this, request);
        eventPublisher.publishEvent(event);
    }

    private static void validateActionRequestForRejection(final ActionRequest actionRequest, final ReimbursementRequest request) {

        if (actionRequest.getStatus().equals(RequestStatus.REJECTED)) {
            if (Objects.isNull(actionRequest.getRejectionReason())) {
                throw new IllegalArgumentException("Rejection reason is required when rejecting a request");
            }

            if (actionRequest.getRejectionReason().equals(OTHERS) &&
                    (Objects.isNull(actionRequest.getComment()) || actionRequest.getComment().isBlank())) {
                throw new IllegalArgumentException(String.format("Comment is required when rejecting a request with reason as %s",
                        actionRequest.getRejectionReason()));
            }
        }
    }
}
