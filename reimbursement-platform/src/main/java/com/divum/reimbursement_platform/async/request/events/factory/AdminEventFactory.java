package com.divum.reimbursement_platform.async.request.events.factory;

import com.divum.reimbursement_platform.async.request.events.RequestApprovedByAdminEvent;
import com.divum.reimbursement_platform.async.request.events.RequestApprovedByManagerEvent;
import com.divum.reimbursement_platform.async.request.events.RequestRejectedByAdminEvent;
import com.divum.reimbursement_platform.async.request.events.RequestRejectedByManagerEvent;
import com.divum.reimbursement_platform.reimbursementRequest.entity.ReimbursementRequest;
import com.divum.reimbursement_platform.reimbursementRequest.entity.RequestStatus;
import org.springframework.context.ApplicationEvent;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.BiFunction;

import static com.divum.reimbursement_platform.reimbursementRequest.entity.RequestStatus.APPROVED;
import static com.divum.reimbursement_platform.reimbursementRequest.entity.RequestStatus.REJECTED;

@Component
public class AdminEventFactory {
    private final Map<RequestStatus, BiFunction<Object, ReimbursementRequest, ApplicationEvent>> map =
            Map.of(
                    APPROVED, RequestApprovedByAdminEvent::new,
                    REJECTED, RequestRejectedByAdminEvent::new
            );

    public ApplicationEvent getEventForAdmin(final Object src, final ReimbursementRequest request) {
        return map.get(request.getStatus()).apply(src, request);
    }
}
