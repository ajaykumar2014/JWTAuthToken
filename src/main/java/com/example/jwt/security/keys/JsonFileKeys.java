package com.example.jwt.security.keys;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Set;

/**
 * It is assumed that the keys will be maintained by administrator so there will not be 2 valid keys available at the same time
 * The class is store and manage keys.
 */
public class JsonFileKeys {

    private final Set<Key<String>> allKeys;

    private static JsonFileKeys keys = new JsonFileKeys();

    private JsonFileKeys() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            allKeys = mapper.readValue(
                    Files.readAllBytes(Paths.get("sec/keys.json")),
                    new TypeReference<Set<Key>>() {});
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("cannot instantiate the object");
        }
    }

    public static JsonFileKeys getInstance() {
        return keys;
    }

    public byte[] getSigningKeyByKeyId(String keyId) {
        for (Key<String> key: allKeys) {
            if( keyId.equals(key.getKeyId()) ) return key.getSigningKey().getBytes();
        }
        return null;
    }

    public byte[] getValidSigningKey() {
        for (Key<String> key: allKeys) {
            if (LocalDate.now().isAfter(key.getValidFrom()) && LocalDate.now().isBefore(key.getValidTo())) {
                return key.getSigningKey().getBytes();
            }
        }
        return null;
    }

    boolean addKey(String keyId, String signingKey, LocalDate validFrom, LocalDate validTo, String keyType) {
        return allKeys.add(new Key<String>(keyId, signingKey, validFrom, validTo, keyType));
    }

    void cleanExpired() {
        for (Key key: allKeys) {
            if(LocalDate.now().isAfter(key.getValidTo())) {
                allKeys.remove(key);
            }
        }
    }


}
