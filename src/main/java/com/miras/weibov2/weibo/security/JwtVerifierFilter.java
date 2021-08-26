package com.miras.weibov2.weibo.security;

//import io.jsonwebtoken.lang.Strings;
//import com.miras.weibov2.weibo.service.JwtService;
import com.miras.weibov2.weibo.dto.TokenPairId;
import com.miras.weibov2.weibo.service.JwtService;
import com.miras.weibov2.weibo.service.TokenPairIdService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;
import redis.clients.jedis.exceptions.JedisConnectionException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
@Component
public class JwtVerifierFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final TokenPairIdService tokenPairIdService;




    @SneakyThrows
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain){
        String authrorizationHeader = httpServletRequest.getHeader("Authorization");
        if(authrorizationHeader == null || !authrorizationHeader.startsWith("Bearer ")){
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;

        }
        String token = authrorizationHeader.replace("Bearer ", "");
        User user = null;
        TokenPairId tokenPairId = null;
        Jws<Claims> claims = null;

        try {
              claims = jwtService.validateTokenAndReturnClaims(token, "access-token");
//        } catch (ExpiredJwtException e) {
////            httpServletResponse.setStatus(401);
////            httpServletResponse.getWriter().write("Expired Token");
//            filterChain.doFilter(httpServletRequest, httpServletResponse);
//            return;

        } catch (Exception e) {
//            httpServletResponse.setStatus(403);
//            httpServletResponse.getWriter().write("Bad token");
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }
        user = jwtService.returnUser(claims);
        tokenPairId = jwtService.tokenPairId(claims);
        try{
        if(!tokenPairIdService.isPresent(tokenPairId) ){
            Authentication authentication = new UsernamePasswordAuthenticationToken(user, "defaultPassword",user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }}
        catch (RedisConnectionFailureException e){
            Authentication authentication = new UsernamePasswordAuthenticationToken(user, "defaultPassword",user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);








    }
}
