package com.divum.reimbursement_platform.security.dao;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {

    private LoginStatus status;

    private String token;
}
