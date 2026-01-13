package com.ktotopawel.opsguard.controller;

import com.ktotopawel.opsguard.dto.LoginRequest;
import com.ktotopawel.opsguard.dto.LoginResponse;
import com.ktotopawel.opsguard.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication Management")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "Login", description = "Authenticates user and returns a JWT token")
    public LoginResponse login(@RequestBody LoginRequest loginRequest) {
        return authService.authenticate(loginRequest);
    }
}
