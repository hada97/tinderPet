package com.mvp.tinderpet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@EnableWebSecurity
@SpringBootApplication
public class  TinderpetApplication {

	public static void main(String[] args) {
		SpringApplication.run(TinderpetApplication.class, args);
		System.out.println("============================");
		System.out.println("========IN EXECUTION========");
		System.out.println("============================");
	}

}
