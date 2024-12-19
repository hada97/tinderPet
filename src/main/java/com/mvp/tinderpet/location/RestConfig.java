package com.mvp.tinderpet.location;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}

//GET http://localhost:8080/geocode?endereco1=New+York&endereco2=Los+Angeles