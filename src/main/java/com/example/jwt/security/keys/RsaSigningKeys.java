package com.example.jwt.security.keys;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.KeyPair;
import java.security.PrivateKey;

@Component
public class RsaSigningKeys {

    private static final String KEY_ID = "jwt-secret";
    private static final String KEY_TYPE = "RS512";

    private Key<PrivateKey> key;


    public  RsaSigningKeys(@Autowired KeyPair keyPair) {
        this.key = new Key<>(KEY_ID, keyPair.getPrivate(), null, null, KEY_TYPE);
    }

    public PrivateKey getValidSigningKey() {
        return key.getSigningKey();
    }

    public String getKeyType() {
        return KEY_TYPE;
    }

}