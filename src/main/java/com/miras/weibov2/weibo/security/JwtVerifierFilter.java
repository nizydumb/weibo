package com.miras.weibov2.weibo.security;

//import io.jsonwebtoken.lang.Strings;
//import com.miras.weibov2.weibo.service.JwtService;
import com.miras.weibov2.weibo.dto.TokenId;
import com.miras.weibov2.weibo.service.JwtService;
import com.miras.weibov2.weibo.service.TokenIdService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@Component
public class JwtVerifierFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final TokenIdService tokenIdService;




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
        TokenId tokenId = null;
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
        tokenId = jwtService.getTokenIdFromClaims(claims);
        try{
        if(!tokenIdService.isPresent(tokenId) ){
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
