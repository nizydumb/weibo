package com.miras.weibov2.weibo.service;

import com.miras.weibov2.weibo.dto.Token;
import com.miras.weibov2.weibo.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final TokenRepository tokenRepository;

    public void save(Token token){
        tokenRepository.save(token);
    }

    public boolean isPresent(Token token){
        return tokenRepository.isPresent(token);
    }

}
