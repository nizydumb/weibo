package com.miras.weibov2.weibo.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
@RequiredArgsConstructor
public class JwtService {

    @Value("${jwt.key.access}")
    String accessTokenKey;

    @Value("${jwt.key.refresh}")
    String refreshTokenKey;


    public String generateToken(String typeOfToken, Authentication authentication) {
        Date issuedTime = new Date();
        Calendar expirationTime = Calendar.getInstance();
        expirationTime.setTime(issuedTime);
        if (typeOfToken == "access-token" ) {
            expirationTime.add(Calendar.MINUTE, 30);
        } else expirationTime.add(Calendar.HOUR_OF_DAY, 24);

        String token = Jwts.builder()
                .setHeaderParam("type", typeOfToken)
                .claim("username", authentication.getName())
                .claim("authorities", authentication.getAuthorities())
                .setIssuedAt(issuedTime)
                .setExpiration(expirationTime.getTime())
                .signWith(Keys.hmacShaKeyFor((typeOfToken == "access-token" ? accessTokenKey : refreshTokenKey).getBytes(StandardCharsets.UTF_8)))
                .compact();
        return token;
    }

    public Jws<Claims> validateTokenAndReturnClaims(String token, String typeOfToken) {
        Jws<Claims> claims = null;

        try {
            claims = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor((typeOfToken == "access-token" ? accessTokenKey : refreshTokenKey).getBytes(StandardCharsets.UTF_8)))
                    .build()
                    .parseClaimsJws(token);
        } catch (ExpiredJwtException e) {
                throw new ResponseStatusException(HttpStatus.CONFLICT,"Token has been expired");
        }
        return claims;
    }
    public org.springframework.security.core.userdetails.User returnUser(String token, String typeOfToken) {

        Jws<Claims> claims = null;

        claims = validateTokenAndReturnClaims(token, typeOfToken );

        List<GrantedAuthority> listOfGrantedAuthorities = new ArrayList<>();
        List<LinkedHashMap<String, String>> stringAuthoritites = (List<LinkedHashMap<String, String>>) claims.getBody().get("authorities");
        for (LinkedHashMap s: stringAuthoritites) {
            listOfGrantedAuthorities.add(new SimpleGrantedAuthority((String) s.get("authority")));
        }

        org.springframework.security.core.userdetails.User user = new org.springframework.security.core.userdetails.User((String) claims.getBody().get("username"),
                "default-password",true,true,true,true, listOfGrantedAuthorities);
        return user;

    }
}

