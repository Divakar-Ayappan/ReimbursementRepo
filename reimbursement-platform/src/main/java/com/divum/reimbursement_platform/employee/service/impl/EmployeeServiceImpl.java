package com.divum.reimbursement_platform.employee.service.impl;

import com.divum.reimbursement_platform.commons.exception.entity.EntityNotFoundException;
import com.divum.reimbursement_platform.employee.dto.AddOrEditEmployeeRequest;
import com.divum.reimbursement_platform.employee.dto.GetAllEmployeeResponse;
import com.divum.reimbursement_platform.employee.dto.GetEmployeeResponse;
import com.divum.reimbursement_platform.employee.entity.Employee;
import com.divum.reimbursement_platform.employee.entity.Status;
import com.divum.reimbursement_platform.employee.repo.EmployeeRepo;
import com.divum.reimbursement_platform.employee.service.EmployeeService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static com.divum.reimbursement_platform.employee.entity.Role.EMPLOYEE;
import static com.divum.reimbursement_platform.employee.entity.Status.ACTIVE;

@Service
@Log4j2
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepo employeeRepo;

    private final PasswordEncoder passwordEncoder;

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
                .managerId(employee.get().getManagerId())
                .build();
    }

    @Override
    public UUID addEmployee(final AddOrEditEmployeeRequest addEmployeeRequest) {
        log.info("Adding employee {}", addEmployeeRequest);

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
                .managerId(addEmployeeRequest.getManagerId())
                .password(passwordEncoder.encode(addEmployeeRequest.getPassword()))
                .build();
        employeeRepo.save(employee);
        return employee.getEmployeeId();
    }

    @Override
    public String updateEmployee(UUID employeeId, AddOrEditEmployeeRequest updateEmployeeRequest) {

        final Optional<Employee> employee = employeeRepo.findById(employeeId);
        if (employee.isEmpty()) {
            throw new EntityNotFoundException("Employee", employeeId.toString());
        }

        final Employee employeeToUpdate = employee.get();
        employeeToUpdate.setFirstName(updateEmployeeRequest.getFirstName());
        employeeToUpdate.setLastName(updateEmployeeRequest.getLastName());
        employeeToUpdate.setEmail(updateEmployeeRequest.getEmail());
        employeeToUpdate.setPhoneNumber(updateEmployeeRequest.getPhoneNumber());
        employeeToUpdate.setDob(updateEmployeeRequest.getDob());
        employeeToUpdate.setStatus(updateEmployeeRequest.getStatus());
        employeeToUpdate.setGender(updateEmployeeRequest.getGender());
        employeeToUpdate.setJoinDate(updateEmployeeRequest.getJoinDate());
        employeeToUpdate.setJobTitle(updateEmployeeRequest.getJobTitle());
        employeeToUpdate.setRole(updateEmployeeRequest.getRole());
        employeeToUpdate.setManagerId(updateEmployeeRequest.getManagerId());
        //TODO Add password changing logic.
//        employeeToUpdate.setPassword();
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

    @Override
    public List<GetAllEmployeeResponse> getAllEmployees() {

        List<GetAllEmployeeResponse> allEmployees = new ArrayList<>();

        for(Employee employee : employeeRepo.findAllByRoleAndStatus(EMPLOYEE, ACTIVE)) {
            allEmployees.add(
                        new GetAllEmployeeResponse(employee.getEmployeeId(), employee.getFirstName(), employee.getLastName())
                );
        }
        return allEmployees;
    }
}
