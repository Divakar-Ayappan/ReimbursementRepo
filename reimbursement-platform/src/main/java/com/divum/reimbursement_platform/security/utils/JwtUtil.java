package com.divum.reimbursement_platform.security.utils;

import com.divum.reimbursement_platform.employee.entity.Role;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    private final String SECRET = "gender,job_title,join_date,last_name,manager_id,password,phone_number,role,status,updated_at,employee_id) values";
    private final long EXPIRATION = 86400000;

    public String generateToken(String username, Role role) {
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role.name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(SignatureAlgorithm.HS256, SECRET.getBytes())
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET.getBytes()).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getEmailFromToken(String token) {
        return Jwts.parser().setSigningKey(SECRET.getBytes()).parseClaimsJws(token).getBody().getSubject();
    }

    public Role getRoleFromToken(String token) {
        String role = Jwts.parser().setSigningKey(SECRET.getBytes()).parseClaimsJws(token).getBody().get("role", String.class);
        return Role.valueOf(role);
    }
}
