package com.miras.weibov2.weibo.service;

import com.miras.weibov2.weibo.dto.Token;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.*;



@Service
@RequiredArgsConstructor
public class JwtService {

    @Value("${jwt.key.access}")
    String accessTokenKey;

    @Value("${jwt.key.refresh}")
    String refreshTokenKey;



    public List<String> generateTokens(Authentication authentication) {
        List<String> tokens = new ArrayList<>();
        Date issuedTime = new Date();
        String uuid = UUID.randomUUID().toString();
        Calendar accessExpirationTime = Calendar.getInstance();
        accessExpirationTime.setTime(issuedTime);
        Calendar refreshExpirationTime = Calendar.getInstance();
        refreshExpirationTime.setTime(issuedTime);
        accessExpirationTime.add(Calendar.MINUTE, 30);
        refreshExpirationTime.add(Calendar.HOUR_OF_DAY, 24);

        String accessToken = Jwts.builder()
                .setHeaderParam("type", "access-token")
                .claim("userId", authentication.getName())
                .claim("authorities", authentication.getAuthorities())
                .claim("tokenId", uuid)
                .setIssuedAt(issuedTime)
                .setExpiration(accessExpirationTime.getTime())
                .signWith(Keys.hmacShaKeyFor(accessTokenKey.getBytes(StandardCharsets.UTF_8)))
                .compact();
        String refreshToken = Jwts.builder()
                .setHeaderParam("type", "refresh-token")
                .claim("userId", authentication.getName())
                .claim("authorities", authentication.getAuthorities())
                .claim("tokenId", uuid)
                .setIssuedAt(issuedTime)
                .setExpiration(refreshExpirationTime.getTime())
                .signWith(Keys.hmacShaKeyFor(refreshTokenKey.getBytes(StandardCharsets.UTF_8)))
                .compact();
        tokens.add(accessToken);
        tokens.add(refreshToken);


        return tokens;

    }

    public Jws<Claims> validateTokenAndReturnClaims(String token, String typeOfToken) throws ExpiredJwtException, Exception{
        Jws<Claims> claims = null;

        claims = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor((typeOfToken == "access-token" ? accessTokenKey : refreshTokenKey).getBytes(StandardCharsets.UTF_8)))
                    .build()
                    .parseClaimsJws(token);

        return claims;
    }
    public org.springframework.security.core.userdetails.User returnUser(Jws<Claims> claims) {

        Collection<GrantedAuthority> listOfGrantedAuthorities = new ArrayList<>();
        List<LinkedHashMap<String, String>> stringAuthoritites = (List<LinkedHashMap<String, String>>) claims.getBody().get("authorities");
        for (LinkedHashMap s: stringAuthoritites) {
            listOfGrantedAuthorities.add(new SimpleGrantedAuthority((String) s.get("authority")));
        }

        org.springframework.security.core.userdetails.User user = new org.springframework.security.core.userdetails.User((String) claims.getBody().get("userId"),
                "default-password",true,true,true,true, listOfGrantedAuthorities);
        return user;

    }
    public Token getTokenIdFromClaims(Jws<Claims> claims){
        Token token = new Token();
        token.setId((String) claims.getBody().get("tokenId"));
        token.setExpiresAt(claims.getBody().getExpiration());
        return token;
    }


}

