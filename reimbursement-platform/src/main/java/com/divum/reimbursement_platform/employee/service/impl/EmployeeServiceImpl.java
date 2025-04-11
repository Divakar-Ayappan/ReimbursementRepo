package com.divum.reimbursement_platform.employee.service.impl;

import com.divum.reimbursement_platform.commons.exception.entity.EntityNotFoundException;
import com.divum.reimbursement_platform.employee.dao.AddOrEditEmployeeRequest;
import com.divum.reimbursement_platform.employee.dao.GetEmployeeResponse;
import com.divum.reimbursement_platform.employee.entity.Employee;
import com.divum.reimbursement_platform.employee.repo.EmployeeRepo;
import com.divum.reimbursement_platform.employee.service.EmployeeService;
import com.divum.reimbursement_platform.projects.dao.GetProjectResponse;
import com.divum.reimbursement_platform.projects.entity.Project;
import com.divum.reimbursement_platform.projects.service.ProjectService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@Log4j2
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepo employeeRepo;

    private final ProjectService projectService;

    @Override
    public GetEmployeeResponse getEmployee(@NonNull final UUID employeeId) {

        final Optional<Employee> employee = employeeRepo.findById(employeeId);
        if (employee.isEmpty()) {
            throw new EntityNotFoundException("Employee", employeeId.toString());
        }

        return GetEmployeeResponse.builder()
                .employeeId(employee.get().getEmployeeId())
                .firstName(employee.get().getFirstName())
                .lastName(employee.get().getLastName())
                .email(employee.get().getEmail())
                .phoneNumber(employee.get().getPhoneNumber())
                .dob(employee.get().getDob())
                .status(employee.get().getStatus())
                .gender(employee.get().getGender())
                .joinDate(employee.get().getJoinDate())
                .jobTitle(employee.get().getJobTitle())
                .role(employee.get().getRole())
                .projectId(employee.get().getProject().getProjectId())
                .build();
    }

    @Override
    public UUID addEmployee(final AddOrEditEmployeeRequest addEmployeeRequest) {
        log.info("Adding employee {}", addEmployeeRequest);

        final GetProjectResponse projectResponse = projectService.getProjectById(addEmployeeRequest.getProjectId());

        final Project project = Project.builder()
                .projectId(projectResponse.getProjectId())
                .build();

        final Employee employee = Employee.builder()
                .employeeId(UUID.randomUUID())
                .firstName(addEmployeeRequest.getFirstName())
                .lastName(addEmployeeRequest.getLastName())
                .email(addEmployeeRequest.getEmail())
                .phoneNumber(addEmployeeRequest.getPhoneNumber())
                .dob(addEmployeeRequest.getDob())
                .status(addEmployeeRequest.getStatus())
                .gender(addEmployeeRequest.getGender())
                .joinDate(addEmployeeRequest.getJoinDate())
                .jobTitle(addEmployeeRequest.getJobTitle())
                .role(addEmployeeRequest.getRole())
                .project(project)
                .build();
        employeeRepo.save(employee);
        return employee.getEmployeeId();
    }

    @Override
    public String updateEmployee(UUID employeeId, AddOrEditEmployeeRequest addEmployeeRequest) {

        final Optional<Employee> employee = employeeRepo.findById(employeeId);
        if (employee.isEmpty()) {
            throw new EntityNotFoundException("Employee", employeeId.toString());
        }

        final Employee employeeToUpdate = employee.get();
        employeeToUpdate.setFirstName(addEmployeeRequest.getFirstName());
        employeeToUpdate.setLastName(addEmployeeRequest.getLastName());
        employeeToUpdate.setEmail(addEmployeeRequest.getEmail());
        employeeToUpdate.setPhoneNumber(addEmployeeRequest.getPhoneNumber());
        employeeToUpdate.setDob(addEmployeeRequest.getDob());
        employeeToUpdate.setStatus(addEmployeeRequest.getStatus());
        employeeToUpdate.setGender(addEmployeeRequest.getGender());
        employeeToUpdate.setJoinDate(addEmployeeRequest.getJoinDate());
        employeeToUpdate.setJobTitle(addEmployeeRequest.getJobTitle());
        employeeToUpdate.setRole(addEmployeeRequest.getRole());
        employeeRepo.save(employeeToUpdate);

        return "Employee Updated Successfully";
    }

    @Override
    public String deleteEmployee(final UUID employeeId) {
        final Optional<Employee> employee = employeeRepo.findById(employeeId);

        if(employee.isEmpty()) {
            throw new EntityNotFoundException("Employee", employeeId.toString());
        }
        log.info("Deleting employee {}", employee);
        employeeRepo.deleteById(employeeId);
        return "Employee Deleted Successfully";
    }
}
