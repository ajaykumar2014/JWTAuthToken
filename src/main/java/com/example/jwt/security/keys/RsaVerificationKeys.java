package com.example.jwt.security.keys;

import org.springframework.core.io.ClassPathResource;
import org.springframework.security.rsa.crypto.KeyStoreKeyFactory;

import java.security.KeyPair;
import java.security.PublicKey;
import java.time.LocalDate;

public class RsaVerificationKeys {

    private static final String KEY_ID = "key1";

    private Key<PublicKey> key;

    private static RsaVerificationKeys keys = new RsaVerificationKeys();

    private RsaVerificationKeys() {

        final KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource("key-private.jks"), "mypass".toCharArray());
        KeyPair keyPair = keyStoreKeyFactory.getKeyPair(KEY_ID);
        key = new Key(KEY_ID, keyPair.getPublic(), LocalDate.now(), LocalDate.now().plusYears(5), "RSA");

    }

}
