package com.mvp.tinderpet.infra.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SpringConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(registry -> {

                    registry.requestMatchers("/", "/login", "/users/register").permitAll();
                    registry.anyRequest().authenticated();
                })
                .oauth2Login(oauth2login -> {
                    oauth2login
                            .loginPage("/login")  // Página de login personalizada
                            .successHandler((request, response, authentication) -> response.sendRedirect("/index"));  // Redirecionamento após login bem-sucedido
                })
                .build();
    }
}


/*
@Bean
public SecurityFilterChain securityFilterChain (HttpSecurity http) throws Exception { return http
.authorizeHttpRequests (registry -> {
}
registry.requestMatchers("/"). permitAll();
registry.anyRequest().authenticated();
})
.oauth2Login(Customizer.withDefaults())
.formLogin(Customizer.withDefaults())
.build();
 */