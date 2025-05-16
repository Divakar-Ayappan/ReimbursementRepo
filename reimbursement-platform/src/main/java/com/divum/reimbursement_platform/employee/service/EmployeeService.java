package com.divum.reimbursement_platform.employee.service;

import com.divum.reimbursement_platform.employee.dto.AddOrEditEmployeeRequest;
import com.divum.reimbursement_platform.employee.dto.GetAllEmployeeResponse;
import com.divum.reimbursement_platform.employee.dto.GetEmployeeResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface EmployeeService {

    GetEmployeeResponse getEmployee(final UUID employeeId);

    UUID addEmployee(final AddOrEditEmployeeRequest addEmployeeRequest);

    String updateEmployee(final UUID employeeId, final AddOrEditEmployeeRequest addEmployeeRequest);

    String  deleteEmployee(final UUID employeeId);

    List<GetAllEmployeeResponse> getAllEmployees();
}
