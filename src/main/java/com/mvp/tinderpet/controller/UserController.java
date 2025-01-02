package com.mvp.tinderpet.controller;

import com.mvp.tinderpet.domain.dog.Dog;
import com.mvp.tinderpet.domain.user.*;
import com.mvp.tinderpet.repository.UserRepository;
import com.mvp.tinderpet.service.DogService;
import com.mvp.tinderpet.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;

@RestController
@RequestMapping("/users")
@SecurityRequirement(name = "bearer-key")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private DogService dogService;

    @GetMapping
    public Page<User> getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return userService.getAll(page, size);
    }

    @GetMapping("/{id}/dogs")
    public Page<Dog> getUsersDogs(
            @PathVariable Long id, // Pega o ID do usuário
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return dogService.getDogsByUserId(id, page, size);
    }


    // Endpoint de login via OAuth2
    @GetMapping("/login/oauth2/code/google")
    public ResponseEntity<UserDetail> loginWithGoogle(OAuth2AuthenticationToken authentication) {
        OAuth2User oAuth2User = authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        if (!userService.existsByEmail(email)) {
            userService.registerNewUser(email, name);
        }
        Long userId = userService.getUserIdByEmailFromGoogle();

        UserDetail userDetail = new UserDetail(name, null);
        return ResponseEntity.ok(userDetail);
    }


    @GetMapping("/login/id")
    public ResponseEntity<Long> userIdByGoogleEmail(OAuth2AuthenticationToken authentication) {
        OAuth2User oAuth2User = authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        Long userId = userService.getUserIdByEmailFromGoogle();
        return ResponseEntity.ok(userId);
    }


    @PatchMapping()
    public ResponseEntity<Void> updateUser(@RequestBody UserUpdateDto data) {
        userService.updateUser(data);
        return ResponseEntity.status(HttpStatus.OK).build();
    }



    @PostMapping("/register")
    public ResponseEntity<UserDetail> createUser(@RequestBody @Valid UserDto user) {

        if (userService.existsByEmail(user.email())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new UserDetail("User already registered", null));
        }
        userService.registerNewUser(user.email(), user.name());
        UserDetail userDetail = new UserDetail(user.name(), user.dogs());

        return ResponseEntity.status(HttpStatus.CREATED).body(userDetail);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        boolean isDeleted = userService.deleteUser(id);
        if (isDeleted) {
            return ResponseEntity.status(302)
                    .header("Location", "/custom_login.html")
                    .build();
        }
        return ResponseEntity.notFound().build();
    }

}