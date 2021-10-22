package com.miras.weibov2.weibo.service;

import com.miras.weibov2.weibo.dto.*;
import com.miras.weibov2.weibo.entity.User;
import com.miras.weibov2.weibo.repository.UserRepository;
import com.miras.weibov2.weibo.security.UserDetailsServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Random;


@Service
@RequiredArgsConstructor
public class AuthService {


    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final JwtService jwtService;
    private final TokenService tokenService;
    private final UserDetailsServiceImpl userDetailsService;




    public AuthResponse login(LoginRequest loginRequest){
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        List<String> tokens = jwtService.generateTokens(authentication);
        return AuthResponse.builder()
                .accessToken(tokens.get(0))
                .refreshToken(tokens.get(1))
                .build();
    }


    public void signup(SignupRequest signupRequest) {

        if(userRepository.findByUsername(signupRequest.getUsername()).isPresent()){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE,"There is already user with username: " + signupRequest.getUsername());
        }
        if(userRepository.findByEmail(signupRequest.getEmail()).isPresent() && userRepository.findByEmail(signupRequest.getEmail()).get().isVerified()){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "There is already user with verified email: " + signupRequest.getEmail());
        }

        String verificationCode = getRandomNumberString();
        User user = new User();
                user.setUsername(signupRequest.getUsername());
                user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
                user.setEmail(signupRequest.getEmail());
                user.setVerificationCode(verificationCode);

        userRepository.save(user);
        try {
            emailService.sendSimpleMessage(user.getEmail(), "You have successfully registered on Weibo", "Verification code: " + verificationCode);
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Email service error");
        }

    }




    public void verifyAccount(VerificationRequest verificationRequest)  {


        User user = userRepository.findByEmail(verificationRequest.getEmail()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid email address"));
        if(user.getVerificationCode().equals(verificationRequest.getVerificationCode())) {
            if(user.isVerified() == true) throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Email is already verified");
            user.setVerified(true);
            userRepository.save(user);
        }
        else throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid verification token");

    }


    public AuthResponse validateAndRefreshToken(String refreshToken) {

        org.springframework.security.core.userdetails.User user = null;

        try {
            Jws<Claims> claims = jwtService.validateTokenAndReturnClaims(refreshToken, "refresh-token");
            user = jwtService.returnUser(claims);
            Token token = jwtService.getTokenIdFromClaims(claims);
            if(tokenService.isPresent(token) ) {
               throw new Exception();
            }

        } catch (ExpiredJwtException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT ,"Expired token");
        } catch (Exception e) {
           throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad token");
        }
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, "defaultPassword", user.getAuthorities());
        List<String> tokens = jwtService.generateTokens(authentication);
        return AuthResponse.builder()
                .accessToken(tokens.get(0))
                .refreshToken(tokens.get(1))
                .build();

    }
    public static String getRandomNumberString() {
        Random rnd = new Random();
        int number = rnd.nextInt(999999);
        return String.format("%06d", number);
    }

    public boolean isUsernameAvailable(String username) {
        return !userRepository.existsUserByUsername(username);
    }
    public boolean isEmailAvailable(String email) {
        return !userRepository.existsUserByEmail(email);
    }

    public boolean isAuthenticated(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return  authentication.isAuthenticated() && (! (authentication instanceof AnonymousAuthenticationToken));
    }

    @PreAuthorize("isAuthenticated()")
    public long getAuthenticatedUserId(){
        return Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
    }


    @PreAuthorize("isAuthenticated()")
    public void changePassword(PasswordChange passwordChange) {
        User user = userRepository.findById(getAuthenticatedUserId()).get();
        if(passwordEncoder.matches(passwordChange.getOldPassword(), user.getPassword())){
            userRepository.updatePassword(user.getId(), passwordEncoder.encode(passwordChange.getNewPassword()));
        }
        else throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong password");

    }
    public void logoutSample(String authorizationHeader) throws Exception {
        String token = authorizationHeader.replace("Bearer ", "");
        Jws<Claims> claims = jwtService.validateTokenAndReturnClaims(token, "access-token");
        Token tokenId = jwtService.getTokenIdFromClaims(claims);
        tokenService.save(tokenId);
    }
}
