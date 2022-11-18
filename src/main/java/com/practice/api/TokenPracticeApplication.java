package com.practice.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.security.Security;

@SpringBootApplication
public class TokenPracticeApplication {

    public static void main(String[] args) {
        Security.setProperty("crypto.policy", "unlimited");
        SpringApplication.run(TokenPracticeApplication.class, args);
    }

}
