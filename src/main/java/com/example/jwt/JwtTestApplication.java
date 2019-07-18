package com.example.jwt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration
public class JwtTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(JwtTestApplication.class, args);
	}

}
