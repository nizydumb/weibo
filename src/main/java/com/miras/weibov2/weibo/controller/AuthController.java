package com.miras.weibov2.weibo.controller;

import com.miras.weibov2.weibo.dto.*;
import com.miras.weibov2.weibo.entity.RefreshToken;
import com.miras.weibov2.weibo.security.LogoutHandlerBean;
import com.miras.weibov2.weibo.service.AuthService;
import com.miras.weibov2.weibo.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;
    private final LogoutHandlerBean logoutHandlerBean;

    @PostMapping("/login")
    ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest){
        return new ResponseEntity<AuthResponse>(authService.login(loginRequest), HttpStatus.OK);
    }

    @PostMapping("/signup")
    ResponseEntity<String> signup(@RequestBody SignupRequest signupRequest) {
        authService.signup(signupRequest);
        return new ResponseEntity<>("User succesfully created", HttpStatus.OK);
    }

    @PostMapping("/account-verification")
    ResponseEntity verifyAccount(@RequestBody VerificationRequest verificationRequest)  {
        authService.verifyAccount(verificationRequest);
        return new ResponseEntity<>("Account succesfully verified", HttpStatus.OK);

    }
    @PostMapping("/refresh-token")
    ResponseEntity validateAndRefreshToken(@RequestParam("token") String refreshToken ){
        AuthResponse authResponse = authService.validateAndRefreshToken(refreshToken);
        return new ResponseEntity(authResponse, HttpStatus.OK);
    }
    @PostMapping("/username-available")
    ResponseEntity isUsernameAvailable(@RequestParam("username") String username){
        if(authService.isUsernameAvailable(username))
            return ResponseEntity.ok().build();
        else return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    }
    @PostMapping("/email-available")
    ResponseEntity isEmailAvailable(@RequestParam("email") String email){
        if(authService.isEmailAvailable(email))
            return ResponseEntity.ok().build();
        else return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    }
    @PostMapping("/password-change")
    ResponseEntity changePassword(@RequestBody PasswordChange passwordChange, @RequestHeader("Authorization") String token ) throws Exception {
        authService.changePassword(passwordChange);
        authService.logoutSample(token);
        return ResponseEntity.ok().build();
    }






}
