package io.nitishc.grievance_portal.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

public class SecurityUtils {

    private SecurityUtils() {
    }

    public static CustomUserDetails getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (CustomUserDetails) auth.getPrincipal();
    }
}

/**
 public static CustomUserDetails getCurrentUser() {
 Authentication auth = SecurityContextHolder.getContext().getAuthentication();
 if (auth != null && auth.getPrincipal() instanceof CustomUserDetails) {
 return (CustomUserDetails) auth.getPrincipal();
 }
 return null;
 }
 */