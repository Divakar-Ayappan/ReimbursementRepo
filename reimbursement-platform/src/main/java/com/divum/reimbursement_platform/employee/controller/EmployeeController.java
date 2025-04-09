package com.divum.reimbursement_platform.employee.controller;

import com.divum.reimbursement_platform.employee.dao.AddOrEditEmployeeRequest;
import com.divum.reimbursement_platform.employee.dao.GetEmployeeResponse;
import com.divum.reimbursement_platform.employee.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
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

@Log4j2
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/{employeeId}")
    public ResponseEntity<GetEmployeeResponse> getEmployeeDetails(@PathVariable("employeeId") final String employeeId){
        log.info("Received request for employee: {}", employeeId);
        return ResponseEntity.ok(employeeService.getEmployee(UUID.fromString(employeeId)));
    }

    @PostMapping("/")
    public ResponseEntity<String> addEmployee(@RequestBody @Valid final AddOrEditEmployeeRequest addOrEditEmployeeRequest){
        log.info("Received request for adding employee: {}",  addOrEditEmployeeRequest);
        return ResponseEntity.ok(employeeService.addEmployee(addOrEditEmployeeRequest));
    }

    @PutMapping("/{employeeId}")
    public ResponseEntity<String> updateEmployee(@PathVariable("employeeId") final UUID employeeId, @RequestBody final AddOrEditEmployeeRequest addOrEditEmployeeRequest){
        log.info("Received request for updating employee: {}, request: {}", addOrEditEmployeeRequest, addOrEditEmployeeRequest);
        return ResponseEntity.ok(employeeService.updateEmployee(employeeId, addOrEditEmployeeRequest));
    }

    @DeleteMapping("/{employeeId}")
    public ResponseEntity<String> deleteEmployee(@PathVariable("employeeId") final UUID employeeId){
        log.info("Received request for deleting employee: {}", employeeId);
        return ResponseEntity.ok(employeeService.deleteEmployee(employeeId));
    }
}
