package com.example.jwt.resources;

import com.example.jwt.model.TokenRequestDto;
import com.example.jwt.security.JwtConstants;
import com.example.jwt.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.sql.Date;
import java.time.Instant;
import java.util.UUID;

@RestController
public class TokenResource {

    @Autowired
    private JwtService jwtService;

    @GetMapping(value = "/v1/exchange/token")
    public ResponseEntity<?> getToken(HttpServletResponse httpServletResponse) {
        TokenRequestDto requestDto = new TokenRequestDto();
        requestDto.setAppData("hello how are you");
        requestDto.setExpiryOffsetInSeconds((int) Date.from(Instant.now()).getTime());
        requestDto.setJourneyType("test");
        requestDto.setTokenId(UUID.randomUUID().toString());

        httpServletResponse.setHeader(JwtConstants.JWT_HEADER_NAME,jwtService.generateToken(requestDto));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/test")
    public ResponseEntity<?> test(){
        return ResponseEntity.ok().build();
    }
}
