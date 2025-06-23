package com.divum.reimbursement_platform.security;

import com.divum.reimbursement_platform.annotations.Authenticated;
import com.divum.reimbursement_platform.annotations.Authorized;
import com.divum.reimbursement_platform.employee.entity.Role;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static com.divum.reimbursement_platform.commons.Constants.JWT_COOKIE_NAME;

@Log4j2
public class AuthAspect {

    private static final List<Role> ROLE_HIERARCHY = List.of(
            Role.EMPLOYEE,
            Role.MANAGER,
            Role.ADMIN
    );


    @Around("@annotation(authenticated)")
    public Object checkAuthentication(ProceedingJoinPoint joinPoint, Authenticated authenticated) throws Throwable {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs == null) {
            log.error("Request attributes not available");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication check failed");
        }

        log.error(attrs.getRequest().getAttribute(JWT_COOKIE_NAME));
        HttpServletRequest request = attrs.getRequest();
        Object userId = request.getAttribute("id");
        log.error(userId);

        if (userId == null) {
            log.debug("Authentication failed for endpoint: {}", joinPoint.getSignature().toShortString());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication required");
        }

        return joinPoint.proceed();
    }

    @Around("@annotation(authorized)")
    public Object checkAuthorization(ProceedingJoinPoint joinPoint, Authorized authorized) throws Throwable {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs == null) {
            log.error("Request attributes not available");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization check failed");
        }

        HttpServletRequest request = attrs.getRequest();
        Role userRole = (Role) request.getAttribute("role");
        Role requiredRole = authorized.value();

        if (userRole == null || !hasRequiredRole(userRole, requiredRole)) {
            log.debug("Authorization failed for user with role {} requiring {}",
                    userRole, requiredRole);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Insufficient permissions");
        }

        return joinPoint.proceed();
    }

    // Support for hierarchical roles (e.g., ADMIN can do anything MANAGER can do)
    private boolean hasRequiredRole(Role userRole, Role requiredRole) {
        int userRoleIndex = ROLE_HIERARCHY.indexOf(userRole);
        int requiredRoleIndex = ROLE_HIERARCHY.indexOf(requiredRole);

        return userRoleIndex >= requiredRoleIndex;
    }

}
