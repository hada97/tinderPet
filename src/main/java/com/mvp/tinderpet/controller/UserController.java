package com.mvp.tinderpet.controller;

import com.mvp.tinderpet.domain.dog.Dog;
import com.mvp.tinderpet.domain.user.*;
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

        // Busca os cachorros do usuário com base no ID
        return dogService.getDogsByUserId(id, page, size);
    }


    // Endpoint de login via OAuth2 (após redirecionamento do Google)
    @GetMapping("/login/oauth2/code/google")
    public ResponseEntity<UserDetail> loginWithGoogle(OAuth2AuthenticationToken authentication) {
        OAuth2User oAuth2User = authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        // Verifica se o usuário já está registrado
        if (!userService.existsByEmail(email)) {
            // Registra o usuário automaticamente
            userService.registerNewUser(email, name);
        }

        // Cria o UserDetail com os dados do usuário
        UserDetail userDetail = new UserDetail(name, null); // Aqui você pode adicionar outras informações

        return ResponseEntity.ok(userDetail);
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
            // Redireciona para a página de sucesso após a exclusão
            return ResponseEntity.status(302)
                    .header("Location", "/custom_login.html")
                    .build();
        }
        // Se o usuário não for encontrado ou não for excluído, pode retornar um código 404 (não encontrado)
        return ResponseEntity.notFound().build();
    }

}