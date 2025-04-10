package com.divum.reimbursement_platform.employee.service;

import com.divum.reimbursement_platform.employee.dao.AddOrEditEmployeeRequest;
import com.divum.reimbursement_platform.employee.dao.GetEmployeeResponse;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface EmployeeService {

    GetEmployeeResponse getEmployee(final UUID employeeId);

    UUID addEmployee(final AddOrEditEmployeeRequest addEmployeeRequest);

    String updateEmployee(final UUID employeeId, final AddOrEditEmployeeRequest addEmployeeRequest);

    String  deleteEmployee(final UUID employeeId);
}
