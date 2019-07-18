package com.example.jwt.security.keys;

import com.example.jwt.security.LocalDateHandler;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

class Key<T> implements Serializable {

    private String keyId;
    private T signingKey;
    private LocalDate validFrom;
    private LocalDate validTo;
    private String keyType;

    private Key() {
    }

    Key(String keyId, T signingKey, LocalDate validFrom, LocalDate validTo, String keyType) {
        this.keyId = keyId;
        this.signingKey = signingKey;
        this.validFrom = validFrom;
        this.validTo = validTo;
        this.keyType = keyType;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }

    public void setSigningKey(T signingKey) {
        this.signingKey = signingKey;
    }

    @JsonDeserialize(using = LocalDateHandler.class)
    public void setValidFrom(LocalDate validFrom) {
        this.validFrom = validFrom;
    }

    @JsonDeserialize(using = LocalDateHandler.class)
    public void setValidTo(LocalDate validTo) {
        this.validTo = validTo;
    }

    public void setKeyType(String keyType) {
        this.keyType = keyType;
    }

    String getKeyId() {
        return keyId;
    }

    T getSigningKey() {
        return signingKey;
    }

    LocalDate getValidTo() {
        return validTo;
    }

    LocalDate getValidFrom() {
        return validFrom;
    }

    String getKeyType() {
        return keyType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Key)) return false;
        Key key = (Key) o;
        return getSigningKey().equals(key.getSigningKey()) &&
                getValidFrom().equals(key.getValidFrom()) &&
                getValidTo().equals(key.getValidTo());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKeyId(), getSigningKey(), getValidFrom(), getValidTo(), getKeyType());
    }
}
