package com.example.jwt.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.rsa.crypto.KeyStoreKeyFactory;

import java.security.KeyPair;

@Configuration
public class ApplicationConfig {

    @Bean
    public KeyPair keyPair() {
       ClassPathResource ksFile = new ClassPathResource("cert/jwt-auth-test.jks");
        KeyStoreKeyFactory ksFactory = new KeyStoreKeyFactory(ksFile, "test123".toCharArray());
        return ksFactory.getKeyPair("jwt-auth-test");

    }
}
