package com.example.jwt.security;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collections;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private JwtService jwtService;

    @Autowired
    JwtAuthorizationFilter(AuthenticationManager authenticationManager, JwtService jwtService) {
        super(authenticationManager);
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpSession httpSession = req.getSession(false);
        System.out.println("Before Session id  [ Filter ] "+ ( httpSession == null ? "SESSION IS NULL":httpSession.getId()) );
        String header = req.getHeader(JwtConstants.JWT_HEADER_NAME);

        if (header == null) {
            chain.doFilter(req, res);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(req);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        chain.doFilter(req, res);
        httpSession = req.getSession(false);
        System.out.println("After Session id  [ Filter ] "+ ( httpSession == null ? "SESSION IS NULL":httpSession.getId()) );

    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {

        String token = request.getHeader(JwtConstants.JWT_HEADER_NAME);

        if (token != null) {
            Claims claims = jwtService.parseToken(token);

            if (claims != null) {
                return new UsernamePasswordAuthenticationToken(claims.getId(),
                        null,
                        Collections.singleton(new SimpleGrantedAuthority(claims.get(JwtConstants.JOURNEY_TYPE).toString())));
            }
            return null;
        }
        return null;
    }


}
