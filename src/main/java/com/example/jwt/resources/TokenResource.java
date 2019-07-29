package com.example.jwt.resources;

import com.example.jwt.model.TokenRequestDto;
import com.example.jwt.security.JwtConstants;
import com.example.jwt.security.JwtService;
import com.nimbusds.jose.jwk.JWKSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Date;
import java.time.Instant;

@RestController
public class TokenResource {

    @Autowired
    private JwtService jwtService;

    @GetMapping(value = "/v1/exchange/token")
    public ResponseEntity<?> getToken(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        HttpSession httpSession = httpServletRequest.getSession();
        System.out.println("Http Session is " + httpSession.getId());
        TokenRequestDto requestDto = new TokenRequestDto();
        requestDto.setAppData("hello how are you");
        requestDto.setExpiryOffsetInSeconds((int) Date.from(Instant.now()).getTime());
        requestDto.setJourneyType("test");
        requestDto.setTokenId(httpSession.getId());

        httpServletResponse.setHeader(JwtConstants.JWT_HEADER_NAME, jwtService.generateToken(requestDto));
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/v1/exchange/refresh")
    public ResponseEntity<?> refreshToken(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        HttpSession httpSession = httpServletRequest.getSession(false);
        System.out.println("Http Session is " + httpSession.getId());
        System.out.println("Http Session isNew " + httpSession.isNew());


        String jwtToken = httpServletRequest.getHeader(JwtConstants.JWT_HEADER_NAME);
        //httpServletResponse.setHeader(JwtConstants.JWT_HEADER_NAME, jwtService.refreshToken(httpServletRequest, jwtToken));
        return ResponseEntity.ok("Refresh token received on current session id");
    }

    @GetMapping("/test")
    public ResponseEntity<?> test() {
        return ResponseEntity.ok().build();
    }
}
