package com.ktotopawel.opsguard.service;

import com.ktotopawel.opsguard.dto.UserRequest;
import com.ktotopawel.opsguard.entity.User;
import com.ktotopawel.opsguard.exception.UserNotFoundException;
import com.ktotopawel.opsguard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    final private UserRepository repository;

    public User createUser(UserRequest userRequest) {
       User newUser = new User();
       newUser.setEmail(userRequest.email());
       newUser.setUsername(userRequest.username());
       newUser.setPassword(userRequest.password());
       return repository.save(newUser);
    }

    public User getUserById(Long id) {
        return repository.findById(id).orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));
    }

    public User getUserByEmail(String email) {
        return repository.findByEmail(email).orElseThrow();
    }

    public void deleteUserById(Long id) {
        repository.deleteById(id);
    }
}
