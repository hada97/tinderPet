package com.mvp.tinderpet.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @RequestMapping("/")
    public String hello() {
        return "Hello";  // Retorna o arquivo login.html (ou .jsp)
    }
}
