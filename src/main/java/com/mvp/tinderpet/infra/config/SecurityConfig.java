package com.mvp.tinderpet.infra.config;

import com.mvp.tinderpet.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

@Configuration
public class SecurityConfig  {

    @Autowired
    private UserService userService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(registry -> {
                    registry.requestMatchers("/", "/login", "/users/register", "/swagger-ui/index.html").permitAll();
                    registry.anyRequest().authenticated();
                })
                .oauth2Login(oauth2login -> {
                    oauth2login
                            .loginPage("/login")  // Página de login personalizada
                            .successHandler((request, response, authentication) -> {
                                OAuth2AuthenticationToken oauth2Token = (OAuth2AuthenticationToken) authentication;
                                OAuth2User oAuth2User = oauth2Token.getPrincipal();
                                String email = oAuth2User.getAttribute("email");
                                String name = oAuth2User.getAttribute("name");

                                // Verificar se o usuário já existe
                                if (!userService.existsByEmail(email)) {
                                    // Registrar o usuário automaticamente caso não exista
                                    userService.registerNewUser(email, name);
                                }

                                // Após o registro (ou não), redireciona para a página desejada
                                response.sendRedirect("/index");  // Redireciona para a página inicial após login
                            });
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