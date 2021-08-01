//package com.miras.weibov2.weibo.security;
//
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.security.Keys;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.time.LocalDate;
//import java.util.Date;
//
//
//public class JwtUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
//
//
//  //  private Object ;
//
//    public JwtUsernamePasswordAuthenticationFilter(AuthenticationManager authenticationManagerBean) {
//        super(authenticationManagerBean);
//    }
//
//    @Override
//    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
//
//      //  TokenResponseDto accessTokenDetails = new TokenResponseDto(authResult.getName(),authResult.getAuthorities(), authResult.getDetails(), authResult.getPrincipal().)
//
//        UsernamePasswordAuthenticationToken authenticationToken = ((UsernamePasswordAuthenticationToken) authResult);
//        String accessToken = Jwts.builder()
//                .claim("type", "access-token")
//                .claim("username", authResult.getName())
//                .claim("authorities", authResult.getAuthorities())
//                //.claim("isEnabled", authenticationToken.)
//                .setIssuedAt(new Date())
//                .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(1)))
//                .signWith(Keys.hmacShaKeyFor("nowyouaresomebodyiusefewfewfewfewfewfewfewfewfwefwefwefwefewfewfewfwfewdtoknow".getBytes()))
//                .compact();
//        String refreshToken = Jwts.builder()
//                .claim("type", "refresh-token")
//                .claim("username", authResult.getName())
//                .claim("authorities", authResult.getAuthorities())
//                //.claim("isEnabled", authenticationToken.)
//                .setIssuedAt(new Date())
//                .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(7)))
//                .signWith(Keys.hmacShaKeyFor("nowyouaresomebodyiusefewfewfewfewfewfewfewfewfwefwefwefwefewfewfewfwfewdtoknow".getBytes()))
//                .compact();
//        response.getWriter().write("access-token: " + accessToken + "\n" + "refresh-token: " + refreshToken);
//
//
//     //   super.successfulAuthentication(request, response, chain, authResult);
//    }
//
//
////    @Override
////    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
////        return super.attemptAuthentication(request, response);
////    }
//
//
//}