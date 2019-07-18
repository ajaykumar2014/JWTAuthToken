package com.example.jwt.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TokenResponseDto {
    private String token;
    private Boolean validated;

    private TokenResponseDto() {
        // for deserialization
    }

    public TokenResponseDto(final String token, final Boolean validated) {
        this.token = token;
        this.validated = validated;
    }
}
