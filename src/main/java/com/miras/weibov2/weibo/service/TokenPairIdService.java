package com.miras.weibov2.weibo.service;

import com.miras.weibov2.weibo.dto.TokenPairId;
import com.miras.weibov2.weibo.repository.TokenPairIdRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenPairIdService {

    private final TokenPairIdRepository tokenPairIdRepository;

    public void save(TokenPairId tokenPairId){
        tokenPairIdRepository.save(tokenPairId);
    }

    public boolean isPresent(TokenPairId tokenPairId){
        return tokenPairIdRepository.isPresent(tokenPairId);
    }

}
