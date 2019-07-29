package com.example.jwt.security;

import com.example.jwt.model.TokenRequestDto;
import com.example.jwt.security.keys.RsaSigningKeys;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.security.KeyPair;
import java.security.PublicKey;
import java.time.Instant;
import java.util.Date;

@Service("jwtService")
public class JwtServiceImpl implements JwtService {

    @Autowired
    private RsaSigningKeys rsaSigningKeys;

    @Autowired
    private KeyPair keyPair;

    @Autowired
    private JWKSet jwkSet;

    @Override
    public String generateToken(TokenRequestDto tokenRequest) {

        Instant now = Instant.now();
        Instant exp = now.plusSeconds(tokenRequest.getExpiryOffsetInSeconds() != 0 ?
                tokenRequest.getExpiryOffsetInSeconds() : JwtConstants.DEFAULT_EXPIRY_OFFSET);


        return Jwts.builder()
                .setHeaderParam("kid", "jwt-secret")
                .setId(tokenRequest.getTokenId())
                .setIssuedAt(Date.from(now))
                .claim(JwtConstants.JOURNEY_TYPE, "OB-JOURNEY")
                .setExpiration(Date.from(Instant.now().plusSeconds(1200)))
                .signWith(SignatureAlgorithm.forName(rsaSigningKeys.getKeyType()), rsaSigningKeys.getValidSigningKey())
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
                .claim(JwtConstants.JOURNEY_TYPE, "OB-JOURNEY")
                .signWith(SignatureAlgorithm.RS512, keyPair.getPrivate())
                .compact();
    }

    @Override
    public String refreshToken(HttpServletRequest httpServletRequest, String token) {
        String sessionId = httpServletRequest.getSession(false).getId();
        Claims claims = parseToken(token);
        if (claims != null && claims.getId().equals(sessionId)) {
            return Jwts.builder()
                    .setId(claims.getId())
                    .setIssuedAt(claims.getIssuedAt())
                    .setExpiration(Date.from(Instant.now().plusSeconds(120)))
                    .setClaims(claims)
                    .signWith(SignatureAlgorithm.RS512, keyPair.getPrivate())
                    .compact();

        }
        return null;
    }


    @Override
    public Claims parseToken(String token) {
        try {
            return Jwts.parser().setSigningKey(keyPair.getPublic()).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Claims parseToken(PublicKey publicKey, String token) {
        try {
            return Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public JWK getSigningInKey(String kid) {
        return jwkSet.getKeyByKeyId(kid);
    }


}
