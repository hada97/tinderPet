package com.mvp.tinderpet.controller;

import com.mvp.tinderpet.domain.user.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@SecurityRequirement(name = "bearer-key")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;


    @GetMapping
    public Page<User> getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return userService.getAll(page, size);
    }

    @PostMapping("/register")
    public ResponseEntity<UserDetail> createUser(@RequestBody @Valid UserDto user) {

        if (userService.existsByEmail(user.email())) {
            // Retorna um Status 409 (Conflict) com uma mensagem informando que o usuário já foi registrado
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new UserDetail("User already registered", null)); // ou outra mensagem personalizada
        }
        userService.registerNewUser(user.email(), user.name());

        UserDetail userDetail = new UserDetail(user.name(), user.dogs());

        return ResponseEntity.status(HttpStatus.CREATED).body(userDetail);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        boolean isDeleted = userService.deleteUser(id);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

}