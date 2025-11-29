package com.ktotopawel.opsguard.controller;

import com.ktotopawel.opsguard.dto.UserRequest;
import com.ktotopawel.opsguard.entity.User;
import com.ktotopawel.opsguard.security.PublicEndpoint;
import com.ktotopawel.opsguard.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "Manage users")
public class UserController {
    private final UserService service;

    @PublicEndpoint
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create user", description = "Creates a user in the db and returns them")
    public User create(@RequestBody UserRequest userRequest) {
        return service.createUser(userRequest);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by id", description = "Returns user with the provided ID")
    public User getUser(@PathVariable Long id) {
        return service.getUserById(id);
    }

    @GetMapping
    @Operation(summary = "Get user by email", description = "Returns the user with the provided email or username, one of which must be passed to the endpoint as a request parameter.")
    public User getUserByUniqueField(@RequestParam(required = false) String email, @RequestParam(required = false) String username) {
        if (email != null && !email.isBlank()) {
            return service.getUserByEmail(email);
        } else if (username != null && !username.isBlank()) {
            return service.getUserByUsername(username);
        } else throw new IllegalArgumentException("Email or username is required");
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user", description = "Hard deletes the user with the provided ID. \n WARNING: This operation is irreversible.")
    public void delete(@PathVariable Long id) {
        service.deleteUserById(id);
    }
}
