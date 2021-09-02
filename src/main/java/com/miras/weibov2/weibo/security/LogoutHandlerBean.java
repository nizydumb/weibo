package com.miras.weibov2.weibo.security;

import com.miras.weibov2.weibo.dto.Token;
import com.miras.weibov2.weibo.service.JwtService;

import com.miras.weibov2.weibo.service.TokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@Component
public class LogoutHandlerBean implements LogoutHandler {
    private final JwtService jwtService;
    private final TokenService tokenIdService;


    @SneakyThrows
    @Override
    public void logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) {
        String authrorizationHeader = httpServletRequest.getHeader("Authorization");
        String token = authrorizationHeader.replace("Bearer ", "");
        Jws<Claims> claims = jwtService.validateTokenAndReturnClaims(token, "access-token");
        Token tokenId = jwtService.getTokenIdFromClaims(claims);
        tokenIdService.save(tokenId);
    }
}
