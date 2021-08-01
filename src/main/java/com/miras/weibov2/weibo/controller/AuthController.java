package com.miras.weibov2.weibo.controller;

import com.miras.weibov2.weibo.dto.AuthResponse;
import com.miras.weibov2.weibo.dto.LoginRequest;
import com.miras.weibov2.weibo.dto.SignupRequest;
import com.miras.weibov2.weibo.dto.VerificationRequest;
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

    @PostMapping("/login")
    ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest){
        return new ResponseEntity<AuthResponse>(authService.login(loginRequest), HttpStatus.OK);
    }

    @PostMapping("/signup")
    ResponseEntity<String> signup(@RequestBody SignupRequest signupRequest) {
        authService.signup(signupRequest);
        return new ResponseEntity<>("User succesfully created", HttpStatus.OK);
    }

    @PostMapping("/accountVerification")
    ResponseEntity<String> verifyAccount(@RequestBody VerificationRequest verificationRequest)  {

        authService.verifyAccount(verificationRequest);
        return new ResponseEntity<>("Account succesfully verified", HttpStatus.OK);

    }
    @GetMapping("/refreshToken/{refreshToken}")
    ResponseEntity validateAndRefreshToken(@PathVariable String refreshToken){
        AuthResponse authResponse = authService.validateAndRefreshToken(refreshToken);
        return new ResponseEntity(authResponse, HttpStatus.OK);
    }
    @PostMapping("/isUsernameAvailable")
    ResponseEntity isUsernameAvailable(@RequestBody Map<String, String> username){
        if(authService.isUsernameAvailable(username.get("username")))
            return ResponseEntity.ok().build();
        else return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    }
    @PostMapping("/isEmailAvailable")
    ResponseEntity isEmailAvailable(@RequestBody Map<String, String> email){
        if(authService.isEmailAvailable(email.get("email")))
            return ResponseEntity.ok().build();
        else return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    }



}
