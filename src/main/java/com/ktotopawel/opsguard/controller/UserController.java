package com.ktotopawel.opsguard.controller;

import com.ktotopawel.opsguard.dto.UserRequest;
import com.ktotopawel.opsguard.entity.User;
import com.ktotopawel.opsguard.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@RequestBody UserRequest userRequest) {
        return service.createUser(userRequest);
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        return service.getUserById(id);
    }

    @GetMapping("/{email}")
    public User getUserByEmail(@PathVariable String email) {
        return service.getUserByEmail(email);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteUserById(id);
    }
}
