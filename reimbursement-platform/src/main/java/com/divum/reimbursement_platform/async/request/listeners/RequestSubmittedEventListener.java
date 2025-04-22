package com.divum.reimbursement_platform.async.request.listeners;

import com.divum.reimbursement_platform.async.request.events.RequestSubmittedEvent;
import com.divum.reimbursement_platform.commons.exception.entity.EntityNotFoundException;
import com.divum.reimbursement_platform.email.EmailService;
import com.divum.reimbursement_platform.employee.entity.Employee;
import com.divum.reimbursement_platform.employee.repo.EmployeeRepo;
import com.divum.reimbursement_platform.reimbursementRequest.entity.ReimbursementRequest;
import com.divum.reimbursement_platform.reimbursementRequest.repo.ReimbursementRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.Map;
import java.util.UUID;

@Log4j2
@Component
@RequiredArgsConstructor
public class RequestSubmittedEventListener {

    private final EmailService emailService;

    private final EmployeeRepo employeeRepo;

    private final ReimbursementRepo reimbursementRepo;

    private static final String SUBJECT = "Reimbursement request submission - Reg.";

    private static final String TEMPLATE_PATH = "/templates/Request Submitted.html";

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMPLETION)
    public void handleRequestSubmittedEventListener(final RequestSubmittedEvent requestSubmittedEvent) {
        log.info("Received event for request submission with request {}", requestSubmittedEvent);

        final UUID requestId = requestSubmittedEvent.getRequest().getRequestId();

        final ReimbursementRequest request = reimbursementRepo.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("Reimbursement request", requestId.toString()));

        final Employee employee = employeeRepo.findById(request.getEmployeeId())
                .orElseThrow(() -> new EntityNotFoundException("Employee",  request.getEmployeeId().toString()));

        final Employee manager = employeeRepo.findById(request.getManagerId())
                .orElseThrow(() -> new EntityNotFoundException("Manager", request.getManagerId().toString()));

        final Map<String, String> replacements = Map.of(
                "managerName", String.format(manager.getFirstName() + manager.getLastName()).trim(),
                "employeeName", String.format(employee.getFirstName() + " " + employee.getLastName()).trim(),
                "amount",  request.getAmount().toString(),
                "submissionDate", request.getCreatedAt().toString(),
                "category", request.getRuleCategory().toString(),
                "description", request.getCommentByRequester()
        );

        emailService.sendReimbursementEmail(manager.getEmail(), SUBJECT, TEMPLATE_PATH, replacements);
    }
}
