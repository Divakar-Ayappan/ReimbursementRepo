package com.divum.reimbursement_platform.async.request.events;

import com.divum.reimbursement_platform.reimbursementRequest.entity.ReimbursementRequest;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class RequestRejectedByAdminEvent extends ApplicationEvent {

    private final ReimbursementRequest request;

    public RequestRejectedByAdminEvent(Object source, ReimbursementRequest request) {
        super(source);
        this.request = request;
    }
}
