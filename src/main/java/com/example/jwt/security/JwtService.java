package com.example.jwt.security;

import com.example.jwt.model.TokenRequestDto;
import com.nimbusds.jose.jwk.JWK;
import io.jsonwebtoken.Claims;

import javax.servlet.http.HttpServletRequest;
import java.security.PublicKey;

public interface JwtService {

    String generateToken(TokenRequestDto tokenRequest) ;

    String updateToken(TokenRequestDto tokenRequest, String token) ;

    String refreshToken(HttpServletRequest httpServletRequest,String token);
    Claims parseToken(String token);
    Claims parseToken(PublicKey publicKey, String token);

    JWK getSigningInKey(String kid);


}
