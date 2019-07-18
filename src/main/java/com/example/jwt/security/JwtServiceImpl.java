package com.example.jwt.security;

import com.example.jwt.model.TokenRequestDto;
import com.example.jwt.security.keys.ConstantKeys;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.security.*;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service("jwtService")
public class JwtServiceImpl implements JwtService {

    static Map KEYS = new HashMap();
    static {

        try {
            KeyPairGenerator keyPairGenerator = null;
            try {
                keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            keyPairGenerator.initialize(2048);

            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            KEYS.put("private", keyPair.getPrivate());
            KEYS.put("public", keyPair.getPublic());
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Autowired
    private ConstantKeys keys;



    @Override
    public String generateToken(TokenRequestDto tokenRequest) {

        Instant now = Instant.now();
        Instant exp = now.plusSeconds(tokenRequest.getExpiryOffsetInSeconds() != 0 ?
                tokenRequest.getExpiryOffsetInSeconds() : JwtConstants.DEFAULT_EXPIRY_OFFSET);


        return Jwts.builder()
                .setId(tokenRequest.getTokenId())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(Instant.now().plusSeconds(120)))
                .claim(JwtConstants.JOURNEY_TYPE, StringUtils.isEmpty(tokenRequest.getJourneyType()) ?
                        JwtConstants.ROLE.INTERNET_CUSTOMER_NTF.getRole() : tokenRequest.getJourneyType())
                .signWith(SignatureAlgorithm.RS256, (PrivateKey)KEYS.get("private"))
                .compact();
    }

    @Override
    public String updateToken(TokenRequestDto tokenRequest, String token) {

        Claims claims = this.parseToken(token);
        Instant now = Instant.now();
        Instant exp = claims.getExpiration().toInstant();

        if (tokenRequest.getCheckThreshold()
                && claims.getExpiration().toInstant().isBefore(now.plusSeconds(JwtConstants.DEFAULT_THRESHOLD))) {
            exp = now.plusSeconds(tokenRequest.getExpiryOffsetInSeconds() != 0 ?
                    tokenRequest.getExpiryOffsetInSeconds() : JwtConstants.DEFAULT_EXPIRY_OFFSET);
        }

        return Jwts.builder()
                .setId(StringUtils.isEmpty(tokenRequest.getTokenId()) ? claims.getId() : tokenRequest.getTokenId())
                .setIssuedAt(claims.getIssuedAt())
                .setExpiration(Date.from(exp))
                .claim(JwtConstants.JOURNEY_TYPE, StringUtils.isEmpty(tokenRequest.getJourneyType()) ?
                        claims.get(JwtConstants.JOURNEY_TYPE) : tokenRequest.getJourneyType())
                .signWith(SignatureAlgorithm.RS256, (PrivateKey) KEYS.get("private"))
                .compact();
    }


    @Override
    public Claims parseToken(String token)  {
        return Jwts.parser().setSigningKey((PublicKey) KEYS.get("public")).parseClaimsJws(token).getBody();
    }



}
