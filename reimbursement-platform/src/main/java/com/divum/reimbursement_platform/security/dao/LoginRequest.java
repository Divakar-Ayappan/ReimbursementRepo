package com.divum.reimbursement_platform.security.dao;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class LoginRequest {

    private String email;
    private String password;
}