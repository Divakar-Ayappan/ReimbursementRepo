package com.divum.reimbursement_platform.security.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static com.divum.reimbursement_platform.commons.Constants.JWT_COOKIE_NAME;

@RequiredArgsConstructor
@Component
public class AuthUtils {

    private final JwtUtil jwtUtil;

    private String getJwtFromCookies(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (JWT_COOKIE_NAME.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    public UUID getEmployeeIdFromRequest(final HttpServletRequest request) {
        final String jwt = getJwtFromCookies(request);

        return jwtUtil.getEmployeeIdFromToken(jwt);
    }

    public String  getEmailIdFromRequest(final HttpServletRequest request) {
        final String jwt = getJwtFromCookies(request);

        return jwtUtil.getEmailFromToken(jwt);
    }
}
