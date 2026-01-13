package com.ktotopawel.opsguard.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;

public class TestSecurityUtils {

    private TestSecurityUtils() {
    }

    public static void setupSecurityContext(Long userId) {
        CustomUserDetails customUserDetails = new CustomUserDetails("username", "password", Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")), userId);
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    public static void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }
}
