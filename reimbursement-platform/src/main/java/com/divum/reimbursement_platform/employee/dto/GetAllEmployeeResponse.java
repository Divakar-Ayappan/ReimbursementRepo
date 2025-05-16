package com.divum.reimbursement_platform.employee.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class GetAllEmployeeResponse {

    private UUID employeeId;

    private String firstName;

    private String lastName;
}
