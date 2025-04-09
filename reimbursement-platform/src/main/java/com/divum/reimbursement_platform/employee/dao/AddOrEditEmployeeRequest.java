package com.divum.reimbursement_platform.employee.dao;

import com.divum.reimbursement_platform.employee.entity.JobTitle;
import com.divum.reimbursement_platform.employee.entity.Role;
import com.divum.reimbursement_platform.employee.entity.Status;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class AddOrEditEmployeeRequest {

    private UUID employeeId;

    @NotBlank(message = "First name is required")
    @Size(max = 50, message = "First name should be less than 50 characters")
    private String firstName;

    @Size(max = 50, message = "Last name should be less than 50 characters")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Phone number is required")
    @Size(min = 10, max = 10, message = "Phone number should be of length 10")
    @Pattern(regexp = "\\d{10}", message = "Phone number must contain exactly 10 digits")
    private String phoneNumber;

    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dob;

    @Pattern(regexp = "^(Male|Female|Other)?$", message = "Gender must be Male, Female, or Other")
    private String gender;

    private LocalDate joinDate;

    @NotNull(message = "Job title is required")
    private JobTitle jobTitle;

    @NotNull(message = "Role is required")
    private Role role;

    @NotNull(message = "Status is required")
    private Status status;
}
