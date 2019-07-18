package com.example.jwt.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@Order(SecurityProperties.BASIC_AUTH_ORDER)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtService loansJwtService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //Jwt Authentication protection on endpoints
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/v1/exchange/token").permitAll() //TODO: add url to ignore list which will generate the token
                .antMatchers(HttpMethod.GET, "/health").permitAll()
                .anyRequest().authenticated()
                .and().addFilterBefore(getAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling().accessDeniedHandler(getAccessDeniedHandler());

        //With JWT token application is immune to CSRF, so disabling it
        http.csrf().disable();

        //Switch off session creation
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    private AccessDeniedHandlerImpl getAccessDeniedHandler() {
        AccessDeniedHandlerImpl handler = new AccessDeniedHandlerImpl();
        handler.setErrorPage(null);
        return handler;
    }

    @DependsOn("loansJwtService")
    private JwtAuthorizationFilter getAuthorizationFilter() throws Exception {
        return new JwtAuthorizationFilter(authenticationManager(), loansJwtService);
    }

}