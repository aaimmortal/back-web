package com.example.webmorda_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class WebmordaBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebmordaBackendApplication.class, args);
    }

}
