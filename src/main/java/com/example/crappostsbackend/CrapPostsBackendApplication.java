package com.example.crappostsbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication
public class CrapPostsBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(CrapPostsBackendApplication.class, args);
    }

}
