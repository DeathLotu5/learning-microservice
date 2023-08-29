package com.appsdeveloperblog.photoapp.api.users.PhotoAppApiUsers.security.filter;

import com.appsdeveloperblog.photoapp.api.users.PhotoAppApiUsers.controllers.DTO.UserDTO;
import com.appsdeveloperblog.photoapp.api.users.PhotoAppApiUsers.controllers.request.LoginRequest;
import com.appsdeveloperblog.photoapp.api.users.PhotoAppApiUsers.service.UsersService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.Objects;

@Component
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final UsersService usersService;
    private final Environment environment;

    public AuthenticationFilter(AuthenticationManager authenticationManager, UsersService usersService, Environment environment) {
        super(authenticationManager);
        this.usersService = usersService;
        this.environment = environment;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            LoginRequest cred = new ObjectMapper().readValue(request.getInputStream(), LoginRequest.class);

            return getAuthenticationManager()
                    .authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    cred.getEmail(),
                                    cred.getPassword(),
                                    new ArrayList<>()
                            )
                    );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        String username = ((User) authResult.getPrincipal()).getUsername();
        UserDTO user = usersService.getUserDetailsByEmail(username);

        String tokenSecret = environment.getProperty("token.secretKey");
        byte[] secretKeyBytes = Base64.getEncoder().encode(tokenSecret.getBytes());

        SecretKey secretKey = new SecretKeySpec(secretKeyBytes, SignatureAlgorithm.HS512.getJcaName());


        long expirationTime = 0;
        String expirationTimeString = environment.getProperty("token.expirationTime");
        if (Objects.nonNull(expirationTimeString)) {
            expirationTime = Long.parseLong(expirationTimeString);
        }

        Instant now = Instant.now();
        String token = Jwts.builder()
                .setSubject(user.getUserId())
                .setExpiration(Date.from(now.plusMillis(expirationTime)))
                .setIssuedAt(Date.from(now))
                .signWith(secretKey, SignatureAlgorithm.ES512)
                .compact();

        response.addHeader("token", token);
        response.addHeader("userId", user.getUserId());
    }
}
