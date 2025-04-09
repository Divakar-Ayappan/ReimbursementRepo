package com.divum.reimbursement_platform.employee.dao;

import com.divum.reimbursement_platform.employee.entity.JobTitle;
import com.divum.reimbursement_platform.employee.entity.Role;
import com.divum.reimbursement_platform.employee.entity.Status;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class GetEmployeeResponse {

    private UUID employeeId;

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;

    private LocalDate dob;

    private String gender;

    private LocalDate joinDate;

    private JobTitle jobTitle;

    private Role role;

    private Status status;
}
