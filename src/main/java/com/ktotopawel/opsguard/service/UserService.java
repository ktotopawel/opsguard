package com.ktotopawel.opsguard.service;

import com.ktotopawel.opsguard.dto.UserRequest;
import com.ktotopawel.opsguard.entity.User;
import com.ktotopawel.opsguard.exception.UserNotFoundException;
import com.ktotopawel.opsguard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    final private UserRepository repository;
    final private PasswordEncoder passwordEncoder;

    public User createUser(UserRequest userRequest) {
       User newUser = new User();
       newUser.setEmail(userRequest.email());
       newUser.setUsername(userRequest.username());
       newUser.setPassword(passwordEncoder.encode(userRequest.password()));
       return repository.save(newUser);
    }

    public User getUserById(Long id) {
        return repository.findById(id).orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));
    }

    public User getUserByEmail(String email) {
        return repository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User with email " + email + " not found"));
    }

    public User getUserByUsername(String username) {
        return repository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User with username " + username + " not found"));
    }

    public void deleteUserById(Long id) {
        repository.deleteById(id);
    }
}
