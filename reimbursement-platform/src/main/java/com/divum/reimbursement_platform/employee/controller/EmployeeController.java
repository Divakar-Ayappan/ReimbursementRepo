package com.divum.reimbursement_platform.employee.controller;

import com.divum.reimbursement_platform.commons.entity.SuccessResponse;
import com.divum.reimbursement_platform.employee.dao.AddOrEditEmployeeRequest;
import com.divum.reimbursement_platform.employee.dao.GetEmployeeResponse;
import com.divum.reimbursement_platform.employee.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static com.divum.reimbursement_platform.commons.entity.StatusCode.CREATED;
import static com.divum.reimbursement_platform.commons.entity.StatusCode.SUCCESS;

@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping("/{employeeId}")
    public ResponseEntity<GetEmployeeResponse> getEmployeeDetails(@PathVariable("employeeId") final UUID employeeId){
        log.info("Received request for fetching employee: {}", employeeId);
        return ResponseEntity.ok(employeeService.getEmployee(employeeId));
    }

    @PostMapping
    public ResponseEntity<SuccessResponse> addEmployee(@RequestBody @Valid final AddOrEditEmployeeRequest addOrEditEmployeeRequest){
        log.info("Received request for adding employee: {}",  addOrEditEmployeeRequest);
        employeeService.addEmployee(addOrEditEmployeeRequest);
        return ResponseEntity.status(HttpStatus.CREATED).
                body(new SuccessResponse("Employee added successfully", CREATED));
    }

    @PutMapping("/{employeeId}")
    public ResponseEntity<SuccessResponse> updateEmployee(@PathVariable("employeeId") final UUID employeeId,
                                                 @RequestBody @Valid final AddOrEditEmployeeRequest addOrEditEmployeeRequest){
        log.info("Received request for updating employee: {}, request: {}", employeeId, addOrEditEmployeeRequest);

        employeeService.updateEmployee(employeeId, addOrEditEmployeeRequest);
        return ResponseEntity.status(HttpStatus.CREATED).
                body(new SuccessResponse("Employee updated successfully", SUCCESS));
    }

    @DeleteMapping("/{employeeId}")
    public ResponseEntity<SuccessResponse> deleteEmployee(@PathVariable("employeeId") final UUID employeeId){
        log.info("Received request for deleting employee: {}", employeeId);
        employeeService.deleteEmployee(employeeId);

        return ResponseEntity.ok(new SuccessResponse("Employee deleted successfully", SUCCESS));
    }
}
