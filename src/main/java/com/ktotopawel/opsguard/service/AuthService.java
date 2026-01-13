package com.ktotopawel.opsguard.service;

import com.ktotopawel.opsguard.dto.LoginRequest;
import com.ktotopawel.opsguard.dto.LoginResponse;
import com.ktotopawel.opsguard.entity.User;
import com.ktotopawel.opsguard.exception.UserNotFoundException;
import com.ktotopawel.opsguard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public LoginResponse authenticate(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );
        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        String token = jwtService.generateToken(user.getUsername());
        return new LoginResponse(token);
    }
}
