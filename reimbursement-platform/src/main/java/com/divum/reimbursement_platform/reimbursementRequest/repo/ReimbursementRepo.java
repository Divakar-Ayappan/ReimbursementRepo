package com.divum.reimbursement_platform.reimbursementRequest.repo;

import com.divum.reimbursement_platform.reimbursementRequest.entity.ReimbursementRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReimbursementRepo extends JpaRepository<ReimbursementRequest, UUID>{

    ReimbursementRequest findByEmployeeId(final UUID employeeId);
}
