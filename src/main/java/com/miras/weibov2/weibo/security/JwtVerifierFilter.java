package com.miras.weibov2.weibo.security;

//import io.jsonwebtoken.lang.Strings;
//import com.miras.weibov2.weibo.service.JwtService;
import com.miras.weibov2.weibo.service.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
@Component
public class JwtVerifierFilter extends OncePerRequestFilter {

    private final JwtService jwtService;




    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String authrorizationHeader = httpServletRequest.getHeader("Authorization");
        if(authrorizationHeader == null || !authrorizationHeader.startsWith("Bearer ")){
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;

        }
        String token = authrorizationHeader.replace("Bearer ", "");
        User user = null;
        try {
            user = jwtService.returnUser(token, "access-token");
        } catch (ExpiredJwtException e) {
            httpServletResponse.setStatus(401);
            httpServletResponse.getWriter().write("Expired Token");
            httpServletResponse.getWriter().flush();

        } catch (Exception e) {
            httpServletResponse.setStatus(400);
            httpServletResponse.getWriter().write("Bad Token");
            httpServletResponse.getWriter().flush();
        }

        Authentication authentication = new UsernamePasswordAuthenticationToken(user, "defaultPassword",user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(httpServletRequest, httpServletResponse);






    }
}
