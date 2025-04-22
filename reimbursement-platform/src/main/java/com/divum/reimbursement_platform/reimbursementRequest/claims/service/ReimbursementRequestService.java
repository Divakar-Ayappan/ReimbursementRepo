package com.divum.reimbursement_platform.reimbursementRequest.claims.service;

import com.divum.reimbursement_platform.reimbursementRequest.claims.dao.AddOrEditReimbursementRequest;
import com.divum.reimbursement_platform.reimbursementRequest.claims.dao.GetReimbursementResponse;
import com.divum.reimbursement_platform.reimbursementRequest.claims.dao.GetRequestsFilter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface ReimbursementRequestService {

    UUID createReimbursementRequest(final AddOrEditReimbursementRequest addOrEditReimbursementRequest);

    GetReimbursementResponse getReimbursementRequestById(final UUID reimbursementRequestId);

    List<GetReimbursementResponse> getReimbursementRequestByEmployeeId(final UUID employeeId, final GetRequestsFilter filter);

    void updateReimbursementRequest(final UUID reimbursementRequestId, final AddOrEditReimbursementRequest addOrEditReimbursementRequest);

    void cancelReimbursementRequest(final UUID reimbursementRequestId);
}
