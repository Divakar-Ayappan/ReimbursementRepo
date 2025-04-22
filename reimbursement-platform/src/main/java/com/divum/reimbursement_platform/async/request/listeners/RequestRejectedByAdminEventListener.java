package com.divum.reimbursement_platform.async.request.listeners;

import com.divum.reimbursement_platform.async.request.events.RequestRejectedByAdminEvent;
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
public class RequestRejectedByAdminEventListener {

    private final EmailService emailService;

    private final EmployeeRepo employeeRepo;

    private final static String SUBJECT = "Reimbursement request rejected by admin- Reg.";

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleRequestRejectedByAdminEvent(final RequestRejectedByAdminEvent rejectedByAdminEvent) {
        log.info("Received event for request rejected by admin with request {}", rejectedByAdminEvent);

        final ReimbursementRequest request = rejectedByAdminEvent.getRequest();

        final Employee employee = employeeRepo.findById(request.getEmployeeId())
                .orElseThrow(() -> new EntityNotFoundException("Employee", request.getEmployeeId().toString()));

        final Map<String, String> replacements = Map.of(
                "employeeName", String.format(employee.getFirstName() + employee.getLastName()).trim(),
                "amount", request.getAmount().toString(),
                "rejectedDate", request.getActionDate().toString(),
                "category", request.getRuleCategory().toString(),
                "rejectedReason", request.getRejectedReason().toString()
        );

        emailService.sendReimbursementEmail(employee.getEmail(), SUBJECT,
                "/templates/Admin Rejection.html", replacements);

    }

}
