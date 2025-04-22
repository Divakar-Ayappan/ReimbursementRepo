package com.divum.reimbursement_platform.reimbursementRequest.actions.service;

import com.divum.reimbursement_platform.reimbursementRequest.actions.dao.ActionRequest;

import java.util.UUID;

public interface RequestActionsService {

    void performActionForManager(final UUID requestId, final ActionRequest actionRequest);

    void performActionForAdmin(final UUID requestId, final ActionRequest actionRequest);
}
