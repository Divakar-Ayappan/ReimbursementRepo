package com.divum.reimbursement_platform.employee.controller;

import com.divum.reimbursement_platform.annotations.Authenticated;
import com.divum.reimbursement_platform.annotations.Authorized;
import com.divum.reimbursement_platform.commons.entity.SuccessResponse;
import com.divum.reimbursement_platform.employee.dao.AddOrEditEmployeeRequest;
import com.divum.reimbursement_platform.employee.dao.GetEmployeeResponse;
import com.divum.reimbursement_platform.employee.entity.Role;
import com.divum.reimbursement_platform.employee.service.EmployeeService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import static com.divum.reimbursement_platform.commons.entity.StatusCode.DELETED;
import static com.divum.reimbursement_platform.commons.entity.StatusCode.UPDATED;
import static com.divum.reimbursement_platform.security.utils.JwtUtil.getEmployeeIdFromToken;

@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping
    @Authenticated
    public ResponseEntity<GetEmployeeResponse> getEmployeeDetails(HttpServletRequest request){
        String token = "";
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("token".equals(cookie.getName())) {
                    System.out.println("Cookiying");
                    token = cookie.getValue();
                    break;
                }
            }
        }
        return ResponseEntity.ok(employeeService.getEmployee(getEmployeeIdFromToken(token)));
    }

    @PostMapping
    @Authenticated
    @Authorized(Role.ADMIN)
    public ResponseEntity<SuccessResponse> addEmployee(@RequestBody @Valid final AddOrEditEmployeeRequest addOrEditEmployeeRequest){
        log.info("Received request for adding employee: {}",  addOrEditEmployeeRequest);
        final UUID employeeId = employeeService.addEmployee(addOrEditEmployeeRequest);
        return ResponseEntity.status(HttpStatus.CREATED).
                body(new SuccessResponse(employeeId.toString(),"Employee added successfully", CREATED));
    }

    @PutMapping("/{employeeId}")
    @Authenticated
    @Authorized(Role.ADMIN)
    public ResponseEntity<SuccessResponse> updateEmployee(@PathVariable("employeeId") final UUID employeeId,
                                                 @RequestBody @Valid final AddOrEditEmployeeRequest addOrEditEmployeeRequest){
        log.info("Received request for updating employee: {}, request: {}", employeeId, addOrEditEmployeeRequest);

        employeeService.updateEmployee(employeeId, addOrEditEmployeeRequest);
        return ResponseEntity.status(HttpStatus.CREATED).
                body(new SuccessResponse("Employee updated successfully", UPDATED));
    }

    @DeleteMapping("/{employeeId}")
    @Authenticated
    @Authorized(Role.ADMIN)
    public ResponseEntity<SuccessResponse> deleteEmployee(@PathVariable("employeeId") final UUID employeeId){
        log.info("Received request for deleting employee: {}", employeeId);
        employeeService.deleteEmployee(employeeId);

        return ResponseEntity.ok(new SuccessResponse("Employee deleted successfully", DELETED));
    }
}
