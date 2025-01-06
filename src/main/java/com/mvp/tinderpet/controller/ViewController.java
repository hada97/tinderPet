package com.mvp.tinderpet.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
public class ViewController {

    @GetMapping("/profile")
    public String profile(OAuth2AuthenticationToken token, Model model) {
        model.addAttribute("name", token.getPrincipal().getAttribute("name"));
        return "profile/profile";
    }

    @GetMapping("/")
    public String login() {
        return "login/custom_login";
    }


    @GetMapping("/index")
    public String index(OAuth2AuthenticationToken token, Model model) {
        model.addAttribute("name", token.getPrincipal().getAttribute("name"));
        return "index/index";
    }

    @GetMapping("/edit")
    public String edit(OAuth2AuthenticationToken token, Model model) {
        model.addAttribute("name", token.getPrincipal().getAttribute("name"));
        return "edit/edit";
    }

    @GetMapping("/api/user/profile")
    public ResponseEntity<Map<String, String>> getUserProfile(OAuth2AuthenticationToken token) {
        String name = token.getPrincipal().getAttribute("name");
        Map<String, String> response = new HashMap<>();
        response.put("name", name);
        return ResponseEntity.ok(response);
    }

}