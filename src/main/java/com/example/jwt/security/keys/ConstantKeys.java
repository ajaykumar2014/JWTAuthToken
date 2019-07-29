package com.example.jwt.security.keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ConstantKeys {

    @Value("${jwt.secret:}")
    private String key;

    public byte[] getValidSigningKey() {
        return key.getBytes();
    }
}
