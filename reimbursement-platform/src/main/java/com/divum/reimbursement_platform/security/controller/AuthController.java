package com.divum.reimbursement_platform.security.controller;

import com.divum.reimbursement_platform.employee.entity.Employee;
import com.divum.reimbursement_platform.employee.repo.EmployeeRepo;
import com.divum.reimbursement_platform.security.dao.LoginResponse;
import com.divum.reimbursement_platform.security.dao.LoginStatus;
import com.divum.reimbursement_platform.security.utils.JwtUtil;
import com.divum.reimbursement_platform.security.dao.LoginRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import static com.divum.reimbursement_platform.commons.Constants.FE_LOCAL_HOST_ENDPOINT;
import static com.divum.reimbursement_platform.commons.Constants.JWT_COOKIE_NAME;
import static com.divum.reimbursement_platform.security.dao.LoginStatus.SUCCESSFUL;

@Log4j2
@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = FE_LOCAL_HOST_ENDPOINT, allowCredentials = "true")
@RequestMapping("/auth")
public class AuthController {

    private final JwtUtil jwtUtil;
    private final EmployeeRepo employeeRepo;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        Optional<Employee> employeeOpt = employeeRepo.findByEmail(loginRequest.getEmail());

        if (employeeOpt.isEmpty()) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email");
        }

        Employee employee = employeeOpt.get();

        if (!passwordEncoder.matches(loginRequest.getPassword(), employee.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password");
        }

        final String token = jwtUtil.generateToken(employee);

        final ResponseCookie cookie = ResponseCookie.from(JWT_COOKIE_NAME, token)
                .httpOnly(true)
                .path("/")
                .maxAge(86400)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.ok(new LoginResponse(SUCCESSFUL,token));
    }

}
