package com.example.jwt.security.keys;

import org.springframework.core.io.ClassPathResource;
import org.springframework.security.rsa.crypto.KeyStoreKeyFactory;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

class RsaSigningKeys {

    private static final String KEY_ID = "key1";

    private final Set<Key<PrivateKey>> allKeys = new HashSet<>();

    private static RsaSigningKeys keys = new RsaSigningKeys();

    private RsaSigningKeys() {
        final KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource("key-private.jks"), "mypass".toCharArray());
        KeyPair keyPair = keyStoreKeyFactory.getKeyPair(KEY_ID);
        allKeys.add(new Key<>(KEY_ID, keyPair.getPrivate(), LocalDate.now(), LocalDate.now().plusYears(5), "RSA"));
    }

}
