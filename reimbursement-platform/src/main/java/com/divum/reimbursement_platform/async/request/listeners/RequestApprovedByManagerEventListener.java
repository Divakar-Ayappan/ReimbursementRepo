package com.divum.reimbursement_platform.async.request.listeners;


import com.divum.reimbursement_platform.async.request.events.RequestApprovedByManagerEvent;
import com.divum.reimbursement_platform.commons.exception.entity.EntityNotFoundException;
import com.divum.reimbursement_platform.email.EmailService;
import com.divum.reimbursement_platform.employee.entity.Employee;
import com.divum.reimbursement_platform.employee.repo.EmployeeRepo;
import com.divum.reimbursement_platform.reimbursementRequest.entity.ReimbursementRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.Map;

import static com.divum.reimbursement_platform.commons.Constants.ADMIN_EMAIL;

@Log4j2
@Component
@RequiredArgsConstructor
public class RequestApprovedByManagerEventListener {

    private final EmailService emailService;

    private final EmployeeRepo employeeRepo;

    private final static String SUBJECT_TO_EMPLOYEE = "Reimbursement request approved by your manager- Reg.";

    private final static String SUBJECT_TO_ADMIN = "Request to review approved reimbursement request - Reg.";

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleRequestApprovedByManagerEvent(final RequestApprovedByManagerEvent approvedByManagerEvent) {
        log.info("Received event for request approved by manager with request {}", approvedByManagerEvent);

        final ReimbursementRequest request = approvedByManagerEvent.getRequest();

        final Employee employee = employeeRepo.findById(request.getEmployeeId())
                .orElseThrow(() -> new EntityNotFoundException("Employee", request.getEmployeeId().toString()));

        final Employee manager = employeeRepo.findById(request.getManagerId())
                .orElseThrow(() -> new EntityNotFoundException("Manager", request.getManagerId().toString()));

        Map<String, String> replacementsForEmployee = Map.of(
                "employeeName", String.format(employee.getFirstName() + employee.getLastName()).trim(),
                "managerName", String.format(manager.getFirstName() + manager.getLastName()).trim(),
                "amount", request.getAmount().toString(),
                "approvedDate", request.getActionDate().toString(),
                "category", request.getRuleCategory().toString()
        );

        emailService.sendReimbursementEmail(employee.getEmail(), SUBJECT_TO_EMPLOYEE,
                "/templates/Manager Approval.html", replacementsForEmployee);


        Map<String, String> replacementsForAdmin = Map.of(
                "employeeName", String.format(employee.getFirstName() + employee.getLastName()).trim(),
                "managerName", String.format(manager.getFirstName() + manager.getLastName()).trim(),
                "amount", request.getAmount().toString(),
                "submissionDate", request.getCreatedAt().toString(),
                "approvedDate", request.getActionDate().toString(),
                "category", request.getRuleCategory().toString(),
                "description", request.getCommentByRequester()
        );

        emailService.sendReimbursementEmail(ADMIN_EMAIL, SUBJECT_TO_ADMIN,
                "/templates/Manager To Admin.html", replacementsForAdmin);

    }
}
