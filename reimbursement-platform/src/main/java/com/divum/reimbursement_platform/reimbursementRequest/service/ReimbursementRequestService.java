package com.divum.reimbursement_platform.reimbursementRequest.service;

import com.divum.reimbursement_platform.reimbursementRequest.dao.AddReimbursementRequest;
import com.divum.reimbursement_platform.reimbursementRequest.dao.GetReimbursementResponse;
import com.divum.reimbursement_platform.reimbursementRequest.dao.GetRequestsFilter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface ReimbursementRequestService {

    UUID createReimbursementRequest(final AddReimbursementRequest addReimbursementRequest);

    GetReimbursementResponse getReimbursementRequestById(final UUID reimbursementRequestId);

    List<GetReimbursementResponse> getReimbursementRequestByEmployeeId(final UUID employeeId, final GetRequestsFilter filter);

    void updateReimbursementRequest(final UUID reimbursementRequestId, final AddReimbursementRequest addReimbursementRequest);

    void cancelReimbursementRequest(final UUID reimbursementRequestId);
}
