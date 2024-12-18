package com.mvp.tinderpet.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class NameController {

    @GetMapping("/profile")
    public ResponseEntity<Map<String, String>> getUserProfile(@AuthenticationPrincipal OAuth2User principal) {
        String userName = principal.getAttribute("name"); // Aqui você pega o nome do usuário
        Map<String, String> response = new HashMap<>();
        response.put("name", userName);
        return ResponseEntity.ok(response);
    }
}
