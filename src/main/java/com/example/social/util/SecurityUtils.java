package com.example.social.util;

import com.example.social.exception.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {


    public static String getCurrentUserKeycloakId() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Jwt) {
            Jwt jwt = (Jwt) authentication.getPrincipal();
            return jwt.getSubject();
        }
        // This should theoretically never be reached if SecurityConfig is set up correctly.
        throw new ApiException("User not authenticated.", HttpStatus.UNAUTHORIZED);
    }
}