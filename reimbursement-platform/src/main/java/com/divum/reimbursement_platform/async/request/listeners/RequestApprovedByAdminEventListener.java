package com.divum.reimbursement_platform.async.request.listeners;

import com.divum.reimbursement_platform.async.request.events.RequestApprovedByAdminEvent;
import com.divum.reimbursement_platform.commons.exception.entity.EntityNotFoundException;
import com.divum.reimbursement_platform.email.EmailService;
import com.divum.reimbursement_platform.employee.entity.Employee;
import com.divum.reimbursement_platform.employee.repo.EmployeeRepo;
import com.divum.reimbursement_platform.reimbursementRequest.entity.ReimbursementRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.Map;

@Log4j2
@Component
@RequiredArgsConstructor
public class RequestApprovedByAdminEventListener {

    private final EmailService emailService;

    private final EmployeeRepo employeeRepo;

    private final static String SUBJECT = "Reimbursement request approved by admin- Reg.";

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleRequestApprovedByAdminEvent(final RequestApprovedByAdminEvent approvedByAdminEvent) {
        log.info("Received event for request approved by admin with request {}", approvedByAdminEvent);

        final ReimbursementRequest request = approvedByAdminEvent.getRequest();

        final Employee employee = employeeRepo.findById(request.getEmployeeId())
                .orElseThrow(() -> new EntityNotFoundException("Employee", request.getEmployeeId().toString()));

        final Map<String, String> replacements = Map.of(
                "employeeName", String.format(employee.getFirstName() + employee.getLastName()).trim(),
                "amount", request.getAmount().toString(),
                "approvedDate", request.getActionDate().toString(),
                "category", request.getRuleCategory().toString()
        );

        emailService.sendReimbursementEmail(employee.getEmail(), SUBJECT,
                "/templates/Admin Approval.html", replacements);

    }
}
