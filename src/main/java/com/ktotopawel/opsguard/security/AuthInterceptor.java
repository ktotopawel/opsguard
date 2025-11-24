package com.ktotopawel.opsguard.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ktotopawel.opsguard.exception.UnathorizedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

    private final ObjectMapper objectMapper;

    @Override
    public boolean preHandle(@NonNull HttpServletRequest req, @NonNull HttpServletResponse res, @NonNull Object handler) throws IOException {
        if (handler instanceof HandlerMethod handlerMethod && handlerMethod.getMethod().isAnnotationPresent(PublicEndpoint.class)) {
            log.info("Accessing public endpoint: {}", req.getRequestURI());
             return true;
        }

        String userIdStr = req.getHeader("X-User-Id");
        log.info("Accessing user id: {}", userIdStr);

        if (userIdStr == null || userIdStr.isBlank()) {
            log.warn("Faiiled auth: Missing user id header");
            throw new UnathorizedException("Missing user id header");
        }

        try {
            Long userId = Long.parseLong(userIdStr);
            UserContext.set(userId);
            return true;
        } catch (NumberFormatException e) {
            log.warn("Failed auth: Invalid user id header: {}", userIdStr);
            throw new UnathorizedException("Invalid user id header");
        }
    }

    @Override
    public void afterCompletion(@NonNull HttpServletRequest req, @NonNull HttpServletResponse res, @NonNull Object handler, Exception ex) {
        UserContext.clear();
    }
}
