package com.divum.reimbursement_platform.security.filter;

import com.divum.reimbursement_platform.employee.entity.Role;
import com.divum.reimbursement_platform.security.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

import static com.divum.reimbursement_platform.commons.Constants.JWT_COOKIE_NAME;

@Component
@Service
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException{

        String token = null;

        // Get token from cookie
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (JWT_COOKIE_NAME.equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        if (token != null && jwtUtil.validateToken(token)) {
            final String email = jwtUtil.getEmailFromToken(token);
            final Role role = jwtUtil.getRoleFromToken(token);
            final UUID userId = jwtUtil.getEmployeeIdFromToken(token);

            // Store it in request, so that the AuthAspect can view these
            // and validate weather the token passed by user is valid or not.
            request.setAttribute("email", email);
            request.setAttribute("role", role);
            request.setAttribute("id", userId);
        }

        filterChain.doFilter(request, response);
    }
}

