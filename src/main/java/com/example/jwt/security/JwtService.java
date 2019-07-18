package com.example.jwt.security;

import com.example.jwt.model.TokenRequestDto;
import io.jsonwebtoken.Claims;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public interface JwtService {

    String generateToken(TokenRequestDto tokenRequest) ;

    String updateToken(TokenRequestDto tokenRequest, String token) ;

    Claims parseToken(String token);

}
